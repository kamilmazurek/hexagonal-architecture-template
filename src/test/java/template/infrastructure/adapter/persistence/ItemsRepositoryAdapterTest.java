package template.infrastructure.adapter.persistence;

import org.junit.jupiter.api.Test;
import template.application.domain.model.Item;
import template.infrastructure.adapter.persistence.model.ItemEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static template.util.TestItems.createTestItemEntities;
import static template.util.TestItems.createTestItems;
import static template.util.TestUtils.once;

public class ItemsRepositoryAdapterTest {

    @Test
    void shouldReadItem() {
        //given item
        var item = ItemEntity.builder().id(1L).name("Item A").build();

        //and repository
        var repository = mock(ItemsRepository.class);
        when(repository.findById(1L)).thenReturn(Optional.of(item));

        //and adapter
        var adapter = new ItemsRepositoryAdapter(repository);

        //when item is requested
        var itemFromRepository = adapter.read(1L);

        //then expected items are returned
        assertEquals(adapter.toDomainObject(item), itemFromRepository.get());

        //and repository was queried for data
        verify(repository, once()).findById(1L);
    }


    @Test
    void shouldReadItems() {
        //given repository
        var repository = mock(ItemsRepository.class);
        when(repository.findAll()).thenReturn(createTestItemEntities());

        //and adapter
        var adapter = new ItemsRepositoryAdapter(repository);

        //when items are requested
        var items = adapter.read();

        //then expected items are returned
        assertEquals(createTestItems(), items);

        //and repository was queried for data
        verify(repository, once()).findAll();
    }

    @Test
    void shouldCreateItem() {
        //given repository
        var repository = mock(ItemsRepository.class);

        //and adapter
        var adapter = new ItemsRepositoryAdapter(repository);

        //and item
        var item = Item.builder().name("Item A").build();

        //when item is created
        adapter.create(item);

        //then item has been put to repository
        var expectedEntity = adapter.toEntity(item);
        expectedEntity.setId(1L);
        verify(repository, once()).save(expectedEntity);
    }

    @Test
    void shouldNotCreateItem() {
        //given repository
        var repository = mock(ItemsRepository.class);

        //and adapter
        var adapter = new ItemsRepositoryAdapter(repository);

        //and item
        var item = Item.builder().id(1L).name("Item A").build();

        //when item is created
        var exception = assertThrows(IllegalArgumentException.class, () -> adapter.create(item));

        //then exception is thrown
        var expectedMessage = "ID should be null, so it will be set by adapter, but it has been already set to 1 instead";
        assertEquals(expectedMessage, exception.getMessage());

        //and item has not been put to repository
        verify(repository, never()).save(any());
    }

    @Test
    void shouldInsertItem() {
        //given repository
        var repository = mock(ItemsRepository.class);

        //and adapter
        var adapter = new ItemsRepositoryAdapter(repository);

        //and item
        var item = Item.builder().id(1L).name("Item A").build();

        //when item is inserted
        adapter.insert(1L, item);

        //then item has been put to repository
        verify(repository, once()).save(adapter.toEntity(item));
    }

}
