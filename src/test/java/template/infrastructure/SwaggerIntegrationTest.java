package template.infrastructure;

import org.junit.jupiter.api.Test;
import template.AbstractIntegrationTest;

import static io.restassured.RestAssured.when;

class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @Test
    void shouldReturnResponseFromSwaggerEndpoint() {
        when()
                .get("/swagger-ui/index.html")
                .then()
                .statusCode(200);
    }

}
