package template.infrastructure;

import org.junit.jupiter.api.Test;
import template.AbstractIntegrationTest;

import static io.restassured.RestAssured.when;

public class OpenApiIntegrationTest extends AbstractIntegrationTest {

    @Test
    void shouldReturnResponseFromOpenApiEndpoint() {
        when()
                .get("/api-docs")
                .then()
                .statusCode(200);
    }
}
