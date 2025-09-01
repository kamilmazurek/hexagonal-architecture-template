package template.infrastructure.adapter.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import template.AbstractIntegrationTest;
import template.api.model.ItemDTO;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static template.util.TestItems.createTestItemDTOs;

class ItemsControllerIntegrationTest extends AbstractIntegrationTest {

    private final ObjectWriter objectWriter = new ObjectMapper().writer();

    @Test
    void shouldGetItem() throws JsonProcessingException {
        when()
                .get("/items/1")
                .then()
                .statusCode(200)
                .assertThat()
                .body(equalTo(objectWriter.writeValueAsString(new ItemDTO().id(1L).name("Item A"))));
    }

    @Test
    void shouldNotFindItem() {
        when()
                .get("/items/4")
                .then()
                .statusCode(404)
                .assertThat()
                .body(emptyString());
    }

    @Test
    void shouldGetItems() throws JsonProcessingException {
        when()
                .get("/items")
                .then()
                .statusCode(200)
                .assertThat()
                .body(equalTo(objectWriter.writeValueAsString(createTestItemDTOs())));
    }

    @Test
    void shouldCreateItemByPostRequest() throws JsonProcessingException {
        //given item
        var item = new ItemDTO().name("Item D");

        //when POST request with item is sent
        given()
                .contentType("application/json")
                .body(item)
                .when()
                .post("/items")
                .then()
                .statusCode(200);

        //then item can be retrieved by ID
        var expectedItem = new ItemDTO().id(4L).name("Item D");
        when()
                .get("/items/4")
                .then()
                .statusCode(200)
                .assertThat()
                .body(equalTo(objectWriter.writeValueAsString(expectedItem)));

        //cleanup
        when()
                .delete("/items/4")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotAcceptPostRequestWhenItemHasID() {
        given()
                .contentType("application/json")
                .body(new ItemDTO().id(4L).name("Item D"))
                .when()
                .post("/items")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldInsertItemByPutRequest() throws JsonProcessingException {
        //given item
        var item = new ItemDTO().id(4L).name("Item D");

        //when PUT request with item is sent
        given()
                .contentType("application/json")
                .body(item)
                .when()
                .put("/items/4")
                .then()
                .statusCode(200);

        //then item can be retrieved by ID
        when()
                .get("/items/4")
                .then()
                .statusCode(200)
                .assertThat()
                .body(equalTo(objectWriter.writeValueAsString(item)));

        //cleanup
        when()
                .delete("/items/4")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotAcceptPutRequestWhenItemHasAmbiguousID() {
        given()
                .contentType("application/json")
                .body(new ItemDTO().id(5L).name("Item E"))
                .when()
                .put("/items/6")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldDeleteItem() throws JsonProcessingException {
        //given item
        var item = new ItemDTO().id(4L).name("Item D");

        //and item has been put
        given()
                .contentType("application/json")
                .body(item)
                .when()
                .put("/items/4")
                .then()
                .statusCode(200);

        //and item can be retrieved by ID
        when()
                .get("/items/4")
                .then()
                .statusCode(200)
                .assertThat()
                .body(equalTo(objectWriter.writeValueAsString(item)));

        //when item is deleted
        when()
                .delete("/items/4")
                .then()
                .statusCode(200);

        //then item can no longer be retrieved by ID
        when()
                .get("/items/4")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldNotFindItemToDelete() {
        when()
                .delete("/items/4")
                .then()
                .statusCode(404);
    }
}
