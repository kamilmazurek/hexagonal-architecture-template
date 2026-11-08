package template.infrastructure.adapter.web;

import org.junit.jupiter.api.Test;
import template.api.model.ItemDTO;
import template.application.domain.model.Item;
import template.application.domain.model.ItemId;
import template.application.port.ItemWebPort;
import template.config.TemplateConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static template.util.TestItems.createTestItemDTOs;
import static template.util.TestItems.createTestItems;

class ItemWebAdapterTest {

    @Test
    void shouldGetItem() {
        //given item
        var item = Item.builder().id(new ItemId("item-a")).name("Item A").build();

        //and port
        var port = mock(ItemWebPort.class);
        when(port.read(new ItemId("item-a"))).thenReturn(Optional.of(item));

        //and adapter
        var adapter = new ItemWebAdapter(port, new TemplateConfig().modelMapper());

        //when item is requested
        var itemFromAdapter = adapter.getItem("item-a");

        //then expected item is returned
        assertEquals(adapter.toDTO(item), itemFromAdapter.get());

        //and port was involved in retrieving the data
        verify(port).read(new ItemId("item-a"));
    }

    @Test
    void shouldNotFindItemWithInvalidId() {
        //given port
        var port = mock(ItemWebPort.class);

        //and adapter
        var adapter = new ItemWebAdapter(port, new TemplateConfig().modelMapper());

        //when item with invalid ID is requested
        var itemFromAdapter = adapter.getItem("Item A");

        //then no item is returned
        assertTrue(itemFromAdapter.isEmpty());

        //and port was not involved in retrieving the data
        verify(port, never()).read(any());
    }

    @Test
    void shouldGetItems() {
        //given port
        var port = mock(ItemWebPort.class);
        when(port.read()).thenReturn(createTestItems());

        //and adapter
        var adapter = new ItemWebAdapter(port, new TemplateConfig().modelMapper());

        //when items are requested
        var items = adapter.getItems();

        //then expected items are returned
        assertEquals(createTestItemDTOs(), items);

        //and port was involved in retrieving the data
        verify(port).read();
    }

    @Test
    void shouldCreateItemByPostRequest() {
        //given port
        var port = mock(ItemWebPort.class);

        //and adapter
        var adapter = new ItemWebAdapter(port, new TemplateConfig().modelMapper());

        //and item
        var item = new ItemDTO().name("Item A");

        //when POST request with item is handled
        adapter.postItem(item);

        //then port was involved in saving the data
        verify(port).create(adapter.toDomainObject(item));
    }

    @Test
    void shouldUpsertItemByPutRequest() {
        //given port
        var port = mock(ItemWebPort.class);

        //and adapter
        var adapter = new ItemWebAdapter(port, new TemplateConfig().modelMapper());

        //and item
        var item = new ItemDTO().id("item-a").name("Item A");

        //when PUT request with item is handled
        adapter.putItem(item);

        //then port was involved in saving the data
        verify(port).upsert(adapter.toDomainObject(item));
    }

    @Test
    void shouldDeleteItem() {
        //given port
        var port = mock(ItemWebPort.class);

        //and adapter
        var adapter = new ItemWebAdapter(port, new TemplateConfig().modelMapper());

        //and item id
        var itemId = "item-a";

        //when DELETE request handled
        adapter.deleteItem(itemId);

        //then port was involved in deleting the data
        verify(port).delete(new ItemId(itemId));
    }

}
