package template.infrastructure.adapter.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import template.AbstractIntegrationTest;
import template.api.model.ItemDTO;
import template.infrastructure.adapter.persistence.ItemsRepository;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.nullValue;
import static template.util.TestItems.createTestItemDTOs;

public class ItemsControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ItemsRepository repository;

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
    void shouldNotGetItem() throws JsonProcessingException {
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
    void shouldPutItem() throws JsonProcessingException {
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
        var itemWithID = new ItemDTO().id(4L).name("Item D");
        when()
                .get("/items/4")
                .then()
                .statusCode(200)
                .assertThat()
                .body(equalTo(objectWriter.writeValueAsString(itemWithID)));

        //cleanup
        //TODO change to DELETE request once implemented
        repository.deleteById(4L);
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
