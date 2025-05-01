package template.adapter.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import template.AbstractIntegrationTest;
import template.api.model.ItemDTO;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static template.util.TestItems.createTestItemDTOs;

public class ItemsControllerIntegrationTest extends AbstractIntegrationTest {

    private final ObjectWriter objectWriter = new ObjectMapper().writer();

    @Test
    void shouldReturnItems() throws JsonProcessingException {
        when()
                .get("/items")
                .then()
                .statusCode(200)
                .assertThat()
                .body(equalTo(objectWriter.writeValueAsString(createTestItemDTOs())));
    }

    @Test
    void shouldPutItem() {
        //given item
        var item = new ItemDTO().name("Item D");

        //when item is put
        given()
                .contentType("application/json")
                .body(item)
                .when()
                .put("/items/4")
                .then()
                .statusCode(200);

        //then item can be retrieved by ID
        //TODO add additional check: retrieve item by ID and check whether it matches previously put item
    }

    @Test
    void shouldNotPutItemIfHasAmbiguousID() {
        given()
                .contentType("application/json")
                .body(new ItemDTO().id(5L).name("Item E"))
                .when()
                .put("/items/6")
                .then()
                .statusCode(400);
    }

}
