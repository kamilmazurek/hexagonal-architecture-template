package template.infrastructure.adapter.web;

import org.junit.jupiter.api.Test;
import template.AbstractIT;
import template.api.model.ItemDTO;
import tools.jackson.databind.ObjectWriter;
import tools.jackson.databind.json.JsonMapper;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static template.util.TestItems.createTestItemDTOs;

class ItemControllerIT extends AbstractIT {

    private final ObjectWriter objectWriter = new JsonMapper().writer();

    @Test
    void shouldGetItem() {
        when()
                .get("/items/item-a")
                .then()
                .statusCode(200)
                .assertThat()
                .body(equalTo(objectWriter.writeValueAsString(new ItemDTO().id("item-a").name("Item A"))));
    }

    @Test
    void shouldNotFindItem() {
        when()
                .get("/items/item-d")
                .then()
                .statusCode(404)
                .assertThat()
                .body(emptyString());
    }

    @Test
    void shouldNotFindItemWithInvalidId() {
        when()
                .get("/items/Item A")
                .then()
                .statusCode(404)
                .assertThat()
                .body(emptyString());
    }

    @Test
    void shouldGetItems() {
        when()
                .get("/items")
                .then()
                .statusCode(200)
                .assertThat()
                .body(equalTo(objectWriter.writeValueAsString(createTestItemDTOs())));
    }

    @Test
    void shouldCreateItemByPostRequest() {
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

        //then item can be retrieved by ID derived from its name
        var expectedItem = new ItemDTO().id("item-d").name("Item D");
        when()
                .get("/items/item-d")
                .then()
                .statusCode(200)
                .assertThat()
                .body(equalTo(objectWriter.writeValueAsString(expectedItem)));

        //cleanup
        when()
                .delete("/items/item-d")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotAcceptPostRequestWhenItemAlreadyExists() {
        given()
                .contentType("application/json")
                .body(new ItemDTO().name("item a"))
                .when()
                .post("/items")
                .then()
                .statusCode(409)
                .body("detail", equalTo("Item with id item-a already exists"));

        //and existing item is not overwritten
        when()
                .get("/items/item-a")
                .then()
                .statusCode(200)
                .body("name", equalTo("Item A"));
    }

    @Test
    void shouldNotAcceptPostRequestWhenItemNameHasNoLettersOrDigits() {
        given()
                .contentType("application/json")
                .body(new ItemDTO().name("!!!"))
                .when()
                .post("/items")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldNotAcceptPostRequestWhenItemNameHasUnsupportedCharacter() {
        given()
                .contentType("application/json")
                .body(new ItemDTO().name("Item Ж"))
                .when()
                .post("/items")
                .then()
                .statusCode(400)
                .body("detail", equalTo("Item name must use Latin letters and digits only, but received unsupported character: ж."));
    }

    @Test
    void shouldNotAcceptPostRequestWhenItemHasID() {
        given()
                .contentType("application/json")
                .body(new ItemDTO().id("item-d").name("Item D"))
                .when()
                .post("/items")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldInsertItemByPutRequest() {
        //given item
        var item = new ItemDTO().id("item-d").name("Item D");

        //when PUT request with item is sent
        given()
                .contentType("application/json")
                .body(item)
                .when()
                .put("/items/item-d")
                .then()
                .statusCode(200);

        //then item can be retrieved by ID
        when()
                .get("/items/item-d")
                .then()
                .statusCode(200)
                .assertThat()
                .body(equalTo(objectWriter.writeValueAsString(item)));

        //cleanup
        when()
                .delete("/items/item-d")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldUpdateItemByPutRequest() {
        //given existing item
        var item = new ItemDTO().id("item-d").name("Item D");
        given()
                .contentType("application/json")
                .body(item)
                .when()
                .put("/items/item-d")
                .then()
                .statusCode(200);

        //when PUT request with updated item is sent
        var updatedItem = new ItemDTO().id("item-d").name("ITEM D");
        given()
                .contentType("application/json")
                .body(updatedItem)
                .when()
                .put("/items/item-d")
                .then()
                .statusCode(200);

        //then updated item can be retrieved by ID
        when()
                .get("/items/item-d")
                .then()
                .statusCode(200)
                .assertThat()
                .body(equalTo(objectWriter.writeValueAsString(updatedItem)));

        //cleanup
        when()
                .delete("/items/item-d")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotAcceptPutRequestWhenItemHasAmbiguousID() {
        given()
                .contentType("application/json")
                .body(new ItemDTO().id("item-e").name("Item E"))
                .when()
                .put("/items/item-f")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldNotAcceptPutRequestWhenIdIsNotDerivedFromName() {
        given()
                .contentType("application/json")
                .body(new ItemDTO().id("item-a").name("Item B"))
                .when()
                .put("/items/item-a")
                .then()
                .statusCode(400)
                .body("detail", equalTo("Item ID must be derived from the item name. Expected: item-b, but received: item-a."));
    }

    @Test
    void shouldDeleteItem() {
        //given existing item
        var item = new ItemDTO().id("item-d").name("Item D");
        given()
                .contentType("application/json")
                .body(item)
                .when()
                .put("/items/item-d")
                .then()
                .statusCode(200);

        //and item can be retrieved by ID
        when()
                .get("/items/item-d")
                .then()
                .statusCode(200)
                .assertThat()
                .body(equalTo(objectWriter.writeValueAsString(item)));

        //when item is deleted
        when()
                .delete("/items/item-d")
                .then()
                .statusCode(200);

        //then item can no longer be retrieved by ID
        when()
                .get("/items/item-d")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldNotAcceptPutRequestWhenItemHasInvalidID() {
        given()
                .contentType("application/json")
                .body(new ItemDTO().id("Item D").name("Item D"))
                .when()
                .put("/items/Item D")
                .then()
                .statusCode(400)
                .body("detail", equalTo("Item ID must consist of lowercase Latin letters and digits separated by single dashes and have at most 100 characters, but received: Item D."));
    }

    @Test
    void shouldNotFindItemToDelete() {
        when()
                .delete("/items/item-d")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldNotFindItemToDeleteWithInvalidId() {
        when()
                .delete("/items/Item D")
                .then()
                .statusCode(404);
    }
}
