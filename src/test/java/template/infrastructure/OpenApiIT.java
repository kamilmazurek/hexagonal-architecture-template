package template.infrastructure;

import org.junit.jupiter.api.Test;
import template.AbstractIT;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

class OpenApiIT extends AbstractIT {

    @Test
    void shouldReturnResponseFromOpenApiEndpoint() {
        when()
                .get("/api-docs")
                .then()
                .statusCode(200)
                .body(containsString("Items API"));
    }

}
