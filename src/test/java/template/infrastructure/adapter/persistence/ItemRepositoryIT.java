package template.infrastructure.adapter.persistence;

import org.junit.jupiter.api.Test;
import template.AbstractIT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static template.util.TestItems.createTestItemEntities;

class ItemRepositoryIT extends AbstractIT {

    @Test
    void shouldFindItem() {
        //when item is requested
        var item = repository.findById("item-a");

        //then expected item is returned
        assertEquals(ItemEntity.builder().id("item-a").name("Item A").build(), item.get());
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
        var item = ItemEntity.builder().id("item-d").name("Item D").build();

        //when item is saved
        repository.save(item);

        //then item can be retrieved from repository
        assertEquals(item, repository.findById(item.getId()).get());

        //cleanup
        repository.deleteById(item.getId());
    }

    @Test
    void shouldDeleteItem() {
        //given item
        var item = ItemEntity.builder().id("item-e").name("Item E").build();

        //and item is in repository
        repository.save(item);
        assertEquals(item, repository.findById(item.getId()).get());

        //when item is deleted
        repository.deleteById(item.getId());

        //then item is no longer in repository
        assertTrue(repository.findById(item.getId()).isEmpty());
    }

}
