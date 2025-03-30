package template.adapter.web;

import org.junit.jupiter.api.Test;
import template.application.port.ItemsPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static template.util.TestItems.createTestItemDTOs;
import static template.util.TestItems.createTestItems;

public class ItemsWebAdapterTest {

    @Test
    void shouldReturnItems() {
        //given port
        var port = mock(ItemsPort.class);
        when(port.getItems()).thenReturn(createTestItems());

        //and adapter
        var adapter = new ItemsWebAdapter(port);

        //when items are requested
        var items = adapter.getItems();

        //then expected items are returned
        assertEquals(createTestItemDTOs(), items);
    }

}
