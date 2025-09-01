package template.infrastructure;

import org.junit.jupiter.api.Test;
import template.AbstractIntegrationTest;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

class ActuatorIntegrationTest extends AbstractIntegrationTest {

    @Test
    void shouldReturnResponseFromActuatorEndpoint() {
        when()
                .get("/actuator")
                .then()
                .statusCode(200)
                .body(containsString("/actuator/health"));
    }

    @Test
    void shouldReturnResponseFromHealthEndpoint() {
        when()
                .get("/actuator/health")
                .then()
                .statusCode(200)
                .body(containsString("{\"status\":\"UP\"}"));
    }

}
