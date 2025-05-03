package template.application.domain.service;

import org.junit.jupiter.api.Test;
import template.application.domain.model.Item;
import template.infrastructure.adapter.persistence.ItemsRepositoryAdapter;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static template.util.TestItems.createTestItems;
import static template.util.TestUtils.once;

public class ItemsServiceTest {

    @Test
    void shouldReadItem() {
        //given item
        var item = Item.builder().id(1L).name("Item A").build();

        //and adapter
        var adapter = mock(ItemsRepositoryAdapter.class);
        when(adapter.read(1L)).thenReturn(Optional.of(item));

        //and service
        var service = new ItemsService(adapter);

        //when item is requested
        var itemFromService = service.read(1L);

        //then expected item is returned
        assertEquals(item, itemFromService.get());

        //and adapter was involved in retrieving the data
        verify(adapter, once()).read(1L);
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
        verify(adapter, once()).read();
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

        //and adapter was involved in saving the data
        verify(adapter, once()).create(item);
    }

    @Test
    void shouldInsertItem() {
        //given adapter
        var adapter = mock(ItemsRepositoryAdapter.class);

        //and service
        var service = new ItemsService(adapter);

        //and item
        var item = Item.builder().id(1L).name("Item A").build();

        //when item is inserted
        service.insert(1L, item);

        //and adapter was involved in saving the data
        verify(adapter, once()).insert(1L, item);
    }

}
