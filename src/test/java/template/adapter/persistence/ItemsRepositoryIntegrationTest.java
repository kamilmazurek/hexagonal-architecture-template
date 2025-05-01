package template.adapter.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import template.AbstractIntegrationTest;
import template.application.domain.model.Item;
import template.infrastructure.adapter.persistence.ItemsRepository;
import template.infrastructure.adapter.persistence.ItemsRepositoryAdapter;
import template.infrastructure.adapter.persistence.model.ItemEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.invokeMethod;
import static template.util.TestItems.createTestItemEntities;

public class ItemsRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ItemsRepository repository;

    @Test
    void shouldReturnItems() {
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
    }

}
