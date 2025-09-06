package template.infrastructure.adapter.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import template.api.model.ItemDTO;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static template.util.TestItems.createTestItemDTOs;

class ItemsControllerTest {

    @Test
    void shouldGetItem() {
        //given item
        var item = new ItemDTO().id(1L).name("Item A");

        //and adapter
        var adapter = mock(ItemsWebAdapter.class);
        when(adapter.getItem(1L)).thenReturn(Optional.of(item));

        //and controller
        var controller = new ItemsController(adapter);

        //when item is requested
        var response = controller.getItem(1L);

        //then response containing expected item is returned
        assertEquals(item, response.getBody());

        //and OK status is returned
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //and adapter was involved in retrieving the data
        verify(adapter).getItem(1L);
    }

    @Test
    void shouldNotFindItem() {
        //given adapter
        var adapter = mock(ItemsWebAdapter.class);
        when(adapter.getItem(1L)).thenReturn(Optional.empty());

        //and controller
        var controller = new ItemsController(adapter);

        //when item is requested
        var response = controller.getItem(1L);

        //then response contains no item
        assertNull(response.getBody());

        //and Not Found status is returned
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        //and adapter was involved in retrieving the data
        verify(adapter).getItem(1L);
    }

    @Test
    void shouldGetItems() {
        //given adapter
        var adapter = mock(ItemsWebAdapter.class);
        when(adapter.getItems()).thenReturn(createTestItemDTOs());

        //and controller
        var controller = new ItemsController(adapter);

        //when items are requested
        var response = controller.getItems();

        //then response containing expected items is returned
        assertEquals(createTestItemDTOs(), response.getBody());

        //and OK status is returned
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //and adapter was involved in retrieving the data
        verify(adapter).getItems();
    }

    @Test
    void shouldCreateItemByPostRequest() {
        //given adapter
        var adapter = mock(ItemsWebAdapter.class);

        //and controller
        var controller = new ItemsController(adapter);

        //and item
        var item = new ItemDTO().name("Item A");

        //when POST request with item is handled
        var response = controller.postItem(item);

        //then OK status is returned
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //and adapter was involved in saving the data
        verify(adapter).postItem(item);
    }

    @Test
    void shouldNotAcceptPostRequestWhenItemHasID() {
        //given adapter
        var adapter = mock(ItemsWebAdapter.class);

        //and controller
        var controller = new ItemsController(adapter);

        //and item
        var item = new ItemDTO().id(1L).name("Item A");

        //when POST request with item containing ID is received
        var response = controller.postItem(item);

        //then Bad Request status is returned
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        //and adapter was not involved in saving the data
        verify(adapter, never()).postItem(any());
    }

    @Test
    void shouldUpsertItemByPutRequest() {
        //given adapter
        var adapter = mock(ItemsWebAdapter.class);

        //and controller
        var controller = new ItemsController(adapter);

        //and item
        var item = new ItemDTO().id(1L).name("Item A");

        //when PUT request with item is handled
        var response = controller.putItem(1L, item);

        //then OK status is returned
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //and adapter was involved in saving the data
        verify(adapter).putItem(1L, item);
    }

    @Test
    void shouldNotPutItemIfHasAmbiguousID() {
        //given adapter
        var adapter = mock(ItemsWebAdapter.class);

        //and controller
        var controller = new ItemsController(adapter);

        //and item
        var item = new ItemDTO().id(1L).name("Item A");

        //when PUT request with ambiguous ID is received
        var response = controller.putItem(2L, item);

        //then Bad Request status is returned
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        //and adapter was not involved in saving the data
        verify(adapter, never()).putItem(any(), any());
    }

    @Test
    void shouldDeleteItem() {
        //given adapter
        var adapter = mock(ItemsWebAdapter.class);

        //and controller
        var controller = new ItemsController(adapter);

        //and item
        var item = new ItemDTO().id(1L).name("Item A");
        when(adapter.getItem(item.getId())).thenReturn(Optional.of(item));

        //when DELETE request is handled
        var response = controller.deleteItem(item.getId());

        //then OK status is returned
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //and adapter was involved in deleting the data
        verify(adapter).deleteItem(item.getId());
    }

    @Test
    void shouldNotFindItemToDelete() {
        //given adapter
        var adapter = mock(ItemsWebAdapter.class);

        //and controller
        var controller = new ItemsController(adapter);

        //and item id
        var itemId = 1L;

        //when DELETE request is handled
        var response = controller.deleteItem(itemId);

        //and Not Found status is returned
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        //and adapter was not involved in deleting the data
        verify(adapter, never()).deleteItem(any());
    }

}
