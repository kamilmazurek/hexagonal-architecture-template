package template.infrastructure.adapter.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import template.AbstractIntegrationTest;
import template.infrastructure.adapter.persistence.model.ItemEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static template.util.TestItems.createTestItemEntities;

public class ItemsRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ItemsRepository repository;

    @Test
    void shouldGetItem() {
        //when item is requested
        var item = repository.findById(1L);

        //then expected item is returned
        assertEquals(ItemEntity.builder().id(1L).name("Item A").build(), item.get());
    }

    @Test
    void shouldGetItems() {
        //when items are requested
        var items = repository.findAll();

        //then expected items are returned
        assertEquals(createTestItemEntities(), items);
    }

    @Test
    void shouldPutItem() {
        //given item
        var item = ItemEntity.builder().id(4L).name("Item D").build();

        //when item is put
        repository.save(item);

        //then item can be retrieved from repository
        var itemFromRepository = repository.findById(4L);
        assertEquals(item.getId(), itemFromRepository.get().getId());
        assertEquals(item.getName(), itemFromRepository.get().getName());

        //cleanup
        repository.deleteById(4L);
    }

}
