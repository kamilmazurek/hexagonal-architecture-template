package template.adapter.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import template.api.model.ItemDTO;
import template.infrastructure.adapter.web.ItemsController;
import template.infrastructure.adapter.web.ItemsWebAdapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static template.util.TestItems.createTestItemDTOs;

public class ItemsControllerTest {

    @Test
    void shouldReturnItems() {
        //given adapter
        var adapter = mock(ItemsWebAdapter.class);
        when(adapter.getItems()).thenReturn(createTestItemDTOs());

        //and controller
        var controller = new ItemsController(adapter);

        //when items are requested
        var response = controller.getItems();

        //then response containing expected items is returned
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createTestItemDTOs(), response.getBody());

        //and adapter was involved in retrieving the data
        verify(adapter, times(1)).getItems();
    }

    @Test
    void shouldPutItem() {
        //given adapter
        var adapter = mock(ItemsWebAdapter.class);

        //and controller
        var controller = new ItemsController(adapter);

        //and item
        var item = new ItemDTO().name("Item A");

        //when item is put
        var response = controller.putItem(1L, item);

        //then OK status is returned
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //and adapter was involved in saving data
        verify(adapter, times(1)).putItem(1L, item);
    }

}
