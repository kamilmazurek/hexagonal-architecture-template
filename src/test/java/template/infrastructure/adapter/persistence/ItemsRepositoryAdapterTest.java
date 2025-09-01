package template.infrastructure.adapter.persistence;

import org.junit.jupiter.api.Test;
import template.application.domain.model.Item;
import template.application.exception.ItemIdAlreadySetException;
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

class ItemsRepositoryAdapterTest {

    @Test
    void shouldReadItem() {
        //given item
        var item = ItemEntity.builder().id(1L).name("Item A").build();

        //and repository
        var repository = mock(ItemsRepository.class);
        when(repository.findById(item.getId())).thenReturn(Optional.of(item));

        //and adapter
        var adapter = new ItemsRepositoryAdapter(repository);

        //when item is requested
        var itemFromRepository = adapter.read(item.getId());

        //then expected item is returned
        assertEquals(adapter.toDomainObject(item), itemFromRepository.get());

        //and repository was queried for data
        verify(repository).findById(item.getId());
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
        verify(repository).findAll();
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

        //then item is saved in repository
        var expectedEntity = adapter.toEntity(item);
        expectedEntity.setId(1L);
        verify(repository).save(expectedEntity);
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
        var exception = assertThrows(ItemIdAlreadySetException.class, () -> adapter.create(item));

        //then exception is thrown
        var expectedMessage = "Item ID must be null when creating a new item. Expected null so the adapter can assign a new ID, but received: 1.";
        assertEquals(expectedMessage, exception.getMessage());

        //and item has not been saved in repository
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
        adapter.insert(item.getId(), item);

        //then item is saved in repository
        verify(repository).save(adapter.toEntity(item));
    }

    @Test
    void shouldDeleteItem() {
        //given repository
        var repository = mock(ItemsRepository.class);

        //and adapter
        var adapter = new ItemsRepositoryAdapter(repository);

        //and item id
        var itemId = 1L;

        //when item is deleted
        adapter.delete(itemId);

        //then item is deleted from repository
        verify(repository).deleteById(itemId);
    }

}
