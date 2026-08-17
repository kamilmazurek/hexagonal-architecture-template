package template.infrastructure;

import org.junit.jupiter.api.Test;
import template.AbstractIT;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

class ActuatorIT extends AbstractIT {

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
