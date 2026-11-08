package template.infrastructure.adapter.persistence;

import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import template.application.domain.model.Item;
import template.application.domain.model.ItemId;
import template.application.exception.ItemAlreadyExistsException;
import template.config.TemplateConfig;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static template.util.TestItems.createTestItemEntities;
import static template.util.TestItems.createTestItems;

class ItemRepositoryAdapterTest {

    @Test
    void shouldReadItem() {
        //given item
        var item = ItemEntity.builder().id("item-a").name("Item A").build();

        //and repository
        var repository = mock(ItemRepository.class);
        when(repository.findById("item-a")).thenReturn(Optional.of(item));

        //and adapter
        var adapter = new ItemRepositoryAdapter(mock(EntityManager.class), repository, new TemplateConfig().modelMapper());

        //when item is requested
        var itemFromRepository = adapter.read(new ItemId("item-a"));

        //then expected item is returned
        assertEquals(adapter.toDomainObject(item), itemFromRepository.get());

        //and repository was queried for data
        verify(repository).findById("item-a");
    }

    @Test
    void shouldReadItems() {
        //given repository
        var repository = mock(ItemRepository.class);
        when(repository.findAll()).thenReturn(createTestItemEntities());

        //and adapter
        var adapter = new ItemRepositoryAdapter(mock(EntityManager.class), repository, new TemplateConfig().modelMapper());

        //when items are requested
        var items = adapter.read();

        //then expected items are returned
        assertEquals(createTestItems(), items);

        //and repository was queried for data
        verify(repository).findAll();
    }

    @Test
    void shouldCreateItem() {
        //given item
        var item = Item.builder().id(new ItemId("item-a")).name("Item A").build();

        //and entity manager
        var entityManager = mock(EntityManager.class);

        //and adapter
        var adapter = new ItemRepositoryAdapter(entityManager, mock(ItemRepository.class), new TemplateConfig().modelMapper());

        //when item is created
        adapter.create(item);

        //then item is persisted and flushed
        verify(entityManager).persist(adapter.toEntity(item));
        verify(entityManager).flush();
    }

    @Test
    void shouldNotCreateItemWhenIdAlreadyExists() {
        //given item
        var item = Item.builder().id(new ItemId("item-a")).name("Item A").build();

        //and entity manager that reports primary key violation
        var entityManager = mock(EntityManager.class);
        doThrow(new ConstraintViolationException("Duplicate key", new SQLException(), "PRIMARY_KEY"))
                .when(entityManager).flush();

        //and adapter
        var adapter = new ItemRepositoryAdapter(entityManager, mock(ItemRepository.class), new TemplateConfig().modelMapper());

        //when item is created
        var exception = assertThrows(ItemAlreadyExistsException.class, () -> adapter.create(item));

        //then exception with item ID is thrown
        assertEquals("Item with id item-a already exists", exception.getMessage());
    }

    @Test
    void shouldUpsertItem() {
        //given repository
        var repository = mock(ItemRepository.class);

        //and adapter
        var adapter = new ItemRepositoryAdapter(mock(EntityManager.class), repository, new TemplateConfig().modelMapper());

        //and item
        var item = Item.builder().id(new ItemId("item-a")).name("Item A").build();

        //when item is upserted
        adapter.upsert(item);

        //then item is saved in repository
        verify(repository).save(adapter.toEntity(item));
    }

    @Test
    void shouldDeleteItem() {
        //given repository
        var repository = mock(ItemRepository.class);

        //and adapter
        var adapter = new ItemRepositoryAdapter(mock(EntityManager.class), repository, new TemplateConfig().modelMapper());

        //and item id
        var itemId = new ItemId("item-a");

        //when item is deleted
        adapter.delete(itemId);

        //then item is deleted from repository
        verify(repository).deleteById("item-a");
    }

}
