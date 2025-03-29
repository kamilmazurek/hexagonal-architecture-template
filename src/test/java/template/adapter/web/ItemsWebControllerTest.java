package template.adapter.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static template.util.TestItems.createTestItemDTOs;

public class ItemsWebControllerTest {

    @Test
    void shouldReturnItems() {
        //given adapter
        var adapter = mock(ItemsWebAdapter.class);
        when(adapter.getItems()).thenReturn(createTestItemDTOs());

        //and controller
        var controller = new ItemsController(adapter);

        //when items are requested
        var response = controller.getItems();

        //then response containing items is returned
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createTestItemDTOs(), response.getBody());

        //and adapter was involved in retrieving the data
        verify(adapter, times(1)).getItems();
    }

}
