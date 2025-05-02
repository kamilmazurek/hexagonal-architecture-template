package template.infrastructure.adapter.web;

import org.junit.jupiter.api.Test;
import template.api.model.ItemDTO;
import template.application.domain.model.Item;
import template.application.port.ItemsWebPort;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static template.util.TestItems.createTestItemDTOs;
import static template.util.TestItems.createTestItems;
import static template.util.TestUtils.once;

public class ItemsWebAdapterTest {

    @Test
    void shouldGetItem() {
        //given item
        var item = Item.builder().id(1L).name("Item A").build();

        //and port
        var port = mock(ItemsWebPort.class);
        when(port.getItem(1L)).thenReturn(Optional.of(item));

        //and adapter
        var adapter = new ItemsWebAdapter(port);

        //when item is requested
        var itemFromAdapter = adapter.getItem(1L);

        //then expected item is returned
        assertEquals(adapter.toDTO(item), itemFromAdapter.get());

        //and port was involved in retrieving the data
        verify(port, once()).getItem(1L);
    }

    @Test
    void shouldGetItems() {
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
        verify(port, once()).getItems();
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
        verify(port, once()).putItem(1L, adapter.toDomainObject(item));
    }

}
