package template.infrastructure;

import org.junit.jupiter.api.Test;
import template.AbstractIntegrationTest;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

class OpenApiIntegrationTest extends AbstractIntegrationTest {

    @Test
    void shouldReturnResponseFromOpenApiEndpoint() {
        when()
                .get("/api-docs")
                .then()
                .statusCode(200)
                .body(containsString("Items API"));
    }

}
