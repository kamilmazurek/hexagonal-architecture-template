package template.infrastructure.adapter.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import template.application.domain.model.Item;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static template.infrastructure.adapter.persistence.Queries.ALTER_SEQUENCE_QUERY;
import static template.infrastructure.adapter.persistence.Queries.CURRENT_SEQ_VAL_QUERY;
import static template.infrastructure.adapter.persistence.Queries.LOCK_QUERY;
import static template.infrastructure.adapter.persistence.Queries.MERGE_QUERY;
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
        //given item
        var item = Item.builder().name("Item A").build();

        //and repository
        var repository = mock(ItemRepository.class);

        //and entity manager
        var entityManager = mock(EntityManager.class);
        var lockQuery = createLockQuery(entityManager);

        //and adapter
        var adapter = new ItemRepositoryAdapter(entityManager, repository, new ModelMapper());

        //when item is created
        adapter.create(item);

        //then lock query is executed
        verify(entityManager).createNativeQuery(LOCK_QUERY);
        verify(lockQuery).getResultList();

        //and item is saved in repository
        verify(repository).save(adapter.toEntity(item));
    }

    @Test
    void shouldUpsertItem() {
        //given repository
        var repository = mock(ItemRepository.class);

        //and entity manager
        var entityManager = mock(EntityManager.class);
        var lockQuery = createLockQuery(entityManager);
        var mergeQuery = createMergeQuery(entityManager);
        var seqQuery = createSequenceQuery(entityManager);
        createAlterSequenceQuery(entityManager, 2L);

        //and adapter
        var adapter = new ItemRepositoryAdapter(entityManager, repository, new ModelMapper());

        //and item
        var item = Item.builder().id(1L).name("Item A").build();

        //when item is upserted
        adapter.upsert(item.getId(), item);

        //then lock query is executed
        verify(entityManager).createNativeQuery(LOCK_QUERY);
        verify(lockQuery).getResultList();

        // and merge query is executed
        verify(entityManager).createNativeQuery(MERGE_QUERY);
        verify(mergeQuery).setParameter(1, item.getId());
        verify(mergeQuery).setParameter(2, item.getName());

        //and sequence query is executed
        verify(seqQuery).getSingleResult();
        verify(entityManager).createNativeQuery(String.format(ALTER_SEQUENCE_QUERY, item.getId() + 1));
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

    private Query createLockQuery(EntityManager em) {
        var query = mock(Query.class);
        when(em.createNativeQuery(LOCK_QUERY)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(1));
        return query;
    }

    private Query createMergeQuery(EntityManager em) {
        var query = mock(Query.class);
        when(em.createNativeQuery(MERGE_QUERY)).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        return query;
    }

    private Query createSequenceQuery(EntityManager em) {
        var query = mock(Query.class);
        when(em.createNativeQuery(CURRENT_SEQ_VAL_QUERY)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(0L);
        return query;
    }

    private void createAlterSequenceQuery(EntityManager em, Long id) {
        var query = mock(Query.class);
        when(em.createNativeQuery(String.format(ALTER_SEQUENCE_QUERY, id))).thenReturn(query);
    }

}
