package template.infrastructure;

import org.junit.jupiter.api.Test;
import template.AbstractIT;

import static io.restassured.RestAssured.when;

class SwaggerIT extends AbstractIT {

    @Test
    void shouldReturnResponseFromSwaggerEndpoint() {
        when()
                .get("/swagger-ui/index.html")
                .then()
                .statusCode(200);
    }

}
