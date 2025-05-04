package template.infrastructure.adapter.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import template.AbstractIntegrationTest;
import template.infrastructure.adapter.persistence.model.ItemEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static template.util.TestItems.createTestItemEntities;

public class ItemsRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ItemsRepository repository;

    @Test
    void shouldFindItem() {
        //when item is requested
        var item = repository.findById(1L);

        //then expected item is returned
        assertEquals(ItemEntity.builder().id(1L).name("Item A").build(), item.get());
    }

    @Test
    void shouldFindItems() {
        //when items are requested
        var items = repository.findAll();

        //then expected items are returned
        assertEquals(createTestItemEntities(), items);
    }

    @Test
    void shouldSaveItem() {
        //given item
        var item = ItemEntity.builder().id(4L).name("Item D").build();

        //when item is saved
        repository.save(item);

        //then item can be retrieved from repository
        var itemFromRepository = repository.findById(item.getId());
        assertEquals(item.getId(), itemFromRepository.get().getId());
        assertEquals(item.getName(), itemFromRepository.get().getName());

        //cleanup
        repository.deleteById(4L);
    }

    @Test
    void shouldDeleteItem() {
        //given item
        var item = ItemEntity.builder().id(4L).name("Item D").build();

        //and item is in repository
        repository.save(item);
        assertEquals(item, repository.findById(item.getId()).get());

        //when item is deleted
        repository.deleteById(item.getId());

        //then item is no longer in repository
        assertTrue(repository.findById(item.getId()).isEmpty());
    }

    @Test
    void shouldFindMaxID() {
        //when repository is queried for max ID
        var maxID = repository.findMaxID();

        //then max ID is returned
        assertEquals(3L, maxID);
    }

}
