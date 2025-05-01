package template.adapter.web;

import org.junit.jupiter.api.Test;
import template.api.model.ItemDTO;
import template.application.port.ItemsWebPort;
import template.infrastructure.adapter.web.ItemsWebAdapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.invokeMethod;
import static template.util.TestItems.createTestItemDTOs;
import static template.util.TestItems.createTestItems;

public class ItemsWebAdapterTest {

    @Test
    void shouldReturnItems() {
        //given port
        var port = mock(ItemsWebPort.class);
        when(port.getItems()).thenReturn(createTestItems());

        //and adapter
        var adapter = new ItemsWebAdapter(port);

        //when items are requested
        var items = adapter.getItems();

        //then expected items are returned
        assertEquals(createTestItemDTOs(), items);

        //and port was involved in retrieving the data
        verify(port, times(1)).getItems();
    }

    @Test
    void shouldPutItem() {
        //given port
        var port = mock(ItemsWebPort.class);

        //and adapter
        var adapter = new ItemsWebAdapter(port);

        //and item
        var item = new ItemDTO().name("Item A");

        //when item is put
        adapter.putItem(1L, item);

        //then port was involved in saving data
        verify(port, times(1)).putItem(1L, invokeMethod(adapter, "toDomainObject", item));
    }

}
