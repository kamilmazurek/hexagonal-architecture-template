#!/usr/bin/env bash
set -euo pipefail

MODE="${1:-all}"
PORT="${PORT:-8080}"
STARTUP_TIMEOUT="${STARTUP_TIMEOUT:-60}"
SKIP_BUILD="${SKIP_BUILD:-false}"
BASE_URL="http://localhost:${PORT}"
IMAGE="template/hexagonal-architecture-template:smoke-test"
CONTAINER="hexagonal-architecture-template-smoke-test"

cd "$(dirname "$0")"
WORK_DIR="$(mktemp -d)"
APP_PID=""
CHECKS=0

log() {
    echo "[smoke-test] $*"
}

fail() {
    echo "[smoke-test] FAILED: $*" >&2
    print_app_logs
    exit 1
}

print_app_logs() {
    if [[ -n "$APP_PID" && -f "$WORK_DIR/app.log" ]]; then
        echo "----- application logs -----" >&2
        tail -n 50 "$WORK_DIR/app.log" >&2
    elif docker container inspect "$CONTAINER" &>/dev/null; then
        echo "----- container logs -----" >&2
        docker logs --tail 50 "$CONTAINER" >&2
    fi
}

stop_app() {
    if [[ -n "$APP_PID" ]]; then
        kill "$APP_PID" 2>/dev/null || true
        wait "$APP_PID" 2>/dev/null || true
        APP_PID=""
    fi
    if docker container inspect "$CONTAINER" &>/dev/null; then
        docker rm -f "$CONTAINER" >/dev/null
    fi
}

cleanup() {
    stop_app
    rm -rf "$WORK_DIR"
}
trap cleanup EXIT

request() {
    local method="$1" path="$2" data="${3:-}"
    local args=(-s -o "$WORK_DIR/body" -w "%{http_code}" -X "$method")
    if [[ -n "$data" ]]; then
        args+=(-H "Content-Type: application/json" -d "$data")
    fi
    STATUS="$(curl "${args[@]}" "${BASE_URL}${path}")" || fail "$method $path - connection error"
    BODY="$(cat "$WORK_DIR/body")"
}

expect() {
    local expected_status="$1" method="$2" path="$3" data="${4:-}"
    shift 4 || shift $#
    request "$method" "$path" "$data"
    [[ "$STATUS" == "$expected_status" ]] || fail "$method $path - expected status $expected_status but was $STATUS, body: $BODY"
    local fragment
    for fragment in "$@"; do
        [[ "$BODY" == *"$fragment"* ]] || fail "$method $path - expected body to contain '$fragment' but was: $BODY"
    done
    CHECKS=$((CHECKS + 1))
    log "OK  $method $path -> $STATUS"
}

wait_until_healthy() {
    log "Waiting up to ${STARTUP_TIMEOUT}s for application to become healthy..."
    local deadline=$((SECONDS + STARTUP_TIMEOUT))
    until curl -sf "${BASE_URL}/actuator/health" 2>/dev/null | grep -q '"status":"UP"'; do
        if [[ -n "$APP_PID" ]] && ! kill -0 "$APP_PID" 2>/dev/null; then
            fail "application process exited during startup"
        fi
        if (( SECONDS >= deadline )); then
            fail "application did not become healthy within ${STARTUP_TIMEOUT}s"
        fi
        sleep 1
    done
    log "Application is up after ${SECONDS}s"
}

run_checks() {
    # actuator
    expect 200 GET /actuator/health "" '"status":"UP"'

    # OpenAPI and Swagger UI
    expect 200 GET /api-docs "" '"title":"Items API"' '"/items"'
    expect 200 GET /swagger-ui/index.html "" 'Swagger UI'

    # items API - default profile starts with no items
    expect 200 GET /items "" '[]'
    expect 404 GET /items/1

    # create
    expect 200 POST /items '{"name":"Smoke Item A"}'
    expect 200 GET /items/1 "" '"id":1' '"name":"Smoke Item A"'
    expect 400 POST /items '{"id":5,"name":"Item with ID"}'

    # insert and update
    expect 200 PUT /items/2 '{"id":2,"name":"Smoke Item B"}'
    expect 200 PUT /items/2 '{"id":2,"name":"Smoke Item B updated"}'
    expect 200 GET /items/2 "" '"name":"Smoke Item B updated"'
    expect 400 PUT /items/3 '{"id":4,"name":"Ambiguous ID"}'
    expect 200 GET /items "" '"Smoke Item A"' '"Smoke Item B updated"'

    # delete
    expect 200 DELETE /items/1
    expect 404 GET /items/1
    expect 404 DELETE /items/1
}

build_jar() {
    if [[ "$SKIP_BUILD" == "true" ]]; then
        log "Skipping build"
    else
        log "Building application..."
        ./mvnw -B -q clean package -DskipTests
    fi
    JAR="$(ls target/hexagonal-architecture-template-*.jar 2>/dev/null | grep -v '\.original$' | head -n 1)"
    [[ -n "$JAR" ]] || fail "no JAR found in target directory"
}

smoke_test_jar() {
    log "=== JAR deployment: $JAR ==="
    java -jar "$JAR" --server.port="$PORT" >"$WORK_DIR/app.log" 2>&1 &
    APP_PID=$!
    SECONDS=0
    wait_until_healthy
    run_checks
    stop_app
}

smoke_test_docker() {
    log "=== Docker deployment: $IMAGE ==="
    command -v docker >/dev/null || fail "docker is not installed"
    docker build -q -t "$IMAGE" . >/dev/null
    docker rm -f "$CONTAINER" &>/dev/null || true
    docker run -d --name "$CONTAINER" -p "${PORT}:8080" "$IMAGE" >/dev/null
    SECONDS=0
    wait_until_healthy

    local user
    user="$(docker exec "$CONTAINER" whoami)"
    [[ "$user" == "template-user" ]] || fail "container runs as '$user' instead of 'template-user'"
    log "OK  container runs as non-root user '$user'"

    run_checks
    stop_app
}

case "$MODE" in
    jar|docker|all) ;;
    *) echo "Usage: $0 [jar|docker|all]" >&2; exit 2 ;;
esac

build_jar
[[ "$MODE" == "docker" ]] || smoke_test_jar
[[ "$MODE" == "jar" ]] || smoke_test_docker
log "PASSED ($CHECKS checks)"
