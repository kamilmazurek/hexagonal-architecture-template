package template.adapter.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import template.AbstractIntegrationTest;
import template.infrastructure.adapter.persistence.ItemsRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

}
