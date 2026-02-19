package template.infrastructure.adapter.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import template.application.domain.model.Item;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static template.util.TestItems.createTestItemEntities;
import static template.util.TestItems.createTestItems;

class ItemRepositoryAdapterTest {

    @Test
    void shouldReadItem() {
        //given item
        var item = ItemEntity.builder().id(1L).name("Item A").build();

        //and repository
        var repository = mock(ItemRepository.class);
        when(repository.findById(item.getId())).thenReturn(Optional.of(item));

        //and adapter
        var adapter = new ItemRepositoryAdapter(mock(EntityManager.class), repository, new ModelMapper());

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
        var repository = mock(ItemRepository.class);
        when(repository.findAll()).thenReturn(createTestItemEntities());

        //and adapter
        var adapter = new ItemRepositoryAdapter(mock(EntityManager.class), repository, new ModelMapper());

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
        var repository = mock(ItemRepository.class);

        //and entity manager
        var entityManager = mock(EntityManager.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(mock(Query.class));

        //and adapter
        var adapter = new ItemRepositoryAdapter(entityManager, repository, new ModelMapper());

        //and item
        var item = Item.builder().name("Item A").build();

        //when item is created
        adapter.create(item);

        //then item is saved in repository
        var expectedEntity = adapter.toEntity(item);
        verify(repository).save(expectedEntity);
    }

    @Test
    void shouldUpsertItem() {
        //given repository
        var repository = mock(ItemRepository.class);

        //and entity manager
        var entityManager = mock(EntityManager.class);
        when(entityManager.createNativeQuery("SELECT 1 FROM ITEM_SEQ_LOCK FOR UPDATE")).thenReturn(mock(Query.class));

        var queryA = mock(Query.class);
        when(queryA.setParameter(eq(1), any())).thenReturn(queryA);
        when(queryA.setParameter(eq(2), any())).thenReturn(queryA);
        when(entityManager.createNativeQuery("MERGE INTO item (id, name) KEY(id) VALUES (?, ?)")).thenReturn(queryA);

        var queryB = mock(Query.class);
        when(queryB.getSingleResult()).thenReturn(1L);
        var currentSeqValQueryString = "SELECT CAST(BASE_VALUE AS BIGINT) FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_NAME = 'ITEM_SEQ'";
        when(entityManager.createNativeQuery(currentSeqValQueryString)).thenReturn(queryB);

        when(entityManager.createNativeQuery("ALTER SEQUENCE ITEM_SEQ RESTART WITH 2")).thenReturn(mock(Query.class));

        //and adapter
        var adapter = new ItemRepositoryAdapter(entityManager, repository, new ModelMapper());

        //and item
        var item = Item.builder().id(1L).name("Item A").build();

        //when item is upserted
        adapter.upsert(item.getId(), item);

        //then item is saved in repository
        verify(entityManager, times(4)).createNativeQuery(anyString());
    }

    @Test
    void shouldDeleteItem() {
        //given repository
        var repository = mock(ItemRepository.class);

        //and adapter
        var adapter = new ItemRepositoryAdapter(mock(EntityManager.class), repository, new ModelMapper());

        //and item id
        var itemId = 1L;

        //when item is deleted
        adapter.delete(itemId);

        //then item is deleted from repository
        verify(repository).deleteById(itemId);
    }

}
