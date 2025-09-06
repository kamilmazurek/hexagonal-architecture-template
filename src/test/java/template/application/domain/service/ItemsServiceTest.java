package template.application.domain.service;

import org.junit.jupiter.api.Test;
import template.application.domain.model.Item;
import template.application.exception.ItemIdAlreadySetException;
import template.infrastructure.adapter.persistence.ItemsRepositoryAdapter;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static template.util.TestItems.createTestItems;

class ItemsServiceTest {

    @Test
    void shouldReadItem() {
        //given item
        var item = Item.builder().id(1L).name("Item A").build();

        //and adapter
        var adapter = mock(ItemsRepositoryAdapter.class);
        when(adapter.read(item.getId())).thenReturn(Optional.of(item));

        //and service
        var service = new ItemsService(adapter);

        //when item is requested
        var itemFromService = service.read(item.getId());

        //then expected item is returned
        assertEquals(item, itemFromService.get());

        //and adapter was involved in retrieving the data
        verify(adapter).read(item.getId());
    }

    @Test
    void shouldReadItems() {
        //given adapter
        var adapter = mock(ItemsRepositoryAdapter.class);
        when(adapter.read()).thenReturn(createTestItems());

        //and service
        var service = new ItemsService(adapter);

        //when items are requested
        var items = service.read();

        //then expected items are returned
        assertEquals(createTestItems(), items);

        //and adapter was involved in retrieving the data
        verify(adapter).read();
    }

    @Test
    void shouldCreateItem() {
        //given adapter
        var adapter = mock(ItemsRepositoryAdapter.class);

        //and service
        var service = new ItemsService(adapter);

        //and item
        var item = Item.builder().name("Item A").build();

        //when item is created
        service.create(item);

        //then adapter is involved in saving the item
        verify(adapter).create(item);
    }

    @Test
    void shouldNotCreateItem() {
        //given adapter
        var adapter = mock(ItemsRepositoryAdapter.class);

        //and service
        var service = new ItemsService(adapter);

        //and item
        var item = Item.builder().id(1L).name("Item A").build();

        //when item is created
        var exception = assertThrows(ItemIdAlreadySetException.class, () -> service.create(item));

        //then exception is thrown
        var expectedMessage = "Item ID must be null when creating a new item. Expected null so the adapter can assign a new ID, but received: 1.";
        assertEquals(expectedMessage, exception.getMessage());

        //and adapter was not involved in saving the item
        verify(adapter, never()).create(item);
    }

    @Test
    void shouldUpsertItem() {
        //given adapter
        var adapter = mock(ItemsRepositoryAdapter.class);

        //and service
        var service = new ItemsService(adapter);

        //and item
        var item = Item.builder().id(1L).name("Item A").build();

        //when item is upserted
        service.upsert(item.getId(), item);

        //then adapter is involved in saving the item
        verify(adapter).upsert(item.getId(), item);
    }

    @Test
    void shouldDeleteItem() {
        //given adapter
        var adapter = mock(ItemsRepositoryAdapter.class);

        //and service
        var service = new ItemsService(adapter);

        //and item id
        var itemId = 1L;

        //when item is deleted
        service.delete(itemId);

        //then adapter is involved in deleting the item
        verify(adapter).delete(itemId);
    }

}
