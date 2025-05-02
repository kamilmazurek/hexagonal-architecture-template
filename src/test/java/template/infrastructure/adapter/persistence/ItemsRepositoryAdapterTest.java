package template.infrastructure.adapter.persistence;

import org.junit.jupiter.api.Test;
import template.application.domain.model.Item;
import template.infrastructure.adapter.persistence.model.ItemEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static template.util.TestItems.createTestItemEntities;
import static template.util.TestItems.createTestItems;
import static template.util.TestUtils.once;

public class ItemsRepositoryAdapterTest {

    @Test
    void shouldGetItem() {
        //given item
        var item = ItemEntity.builder().id(1L).name("Item A").build();

        //and repository
        var repository = mock(ItemsRepository.class);
        when(repository.findById(1L)).thenReturn(Optional.of(item));

        //and adapter
        var adapter = new ItemsRepositoryAdapter(repository);

        //when item is requested
        var itemFromRepository = adapter.getItem(1L);

        //then expected items are returned
        assertEquals(adapter.toDomainObject(item), itemFromRepository.get());

        //and repository was queried for data
        verify(repository, once()).findById(1L);
    }


    @Test
    void shouldGetItems() {
        //given repository
        var repository = mock(ItemsRepository.class);
        when(repository.findAll()).thenReturn(createTestItemEntities());

        //and adapter
        var adapter = new ItemsRepositoryAdapter(repository);

        //when items are requested
        var items = adapter.getItems();

        //then expected items are returned
        assertEquals(createTestItems(), items);

        //and repository was queried for data
        verify(repository, once()).findAll();
    }

    @Test
    void shouldPutItem() {
        //given repository
        var repository = mock(ItemsRepository.class);

        //and adapter
        var adapter = new ItemsRepositoryAdapter(repository);

        //and item
        var item = Item.builder().name("Item A").build();

        //when item is put
        adapter.putItem(1L, item);

        //then item has been put to repository
        verify(repository, times(1)).save(adapter.toEntity(item));
    }

}
