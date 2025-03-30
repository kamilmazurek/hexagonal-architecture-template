package template.application.service;

import org.junit.jupiter.api.Test;
import template.application.domain.service.ItemsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static template.util.TestItems.createTestItems;

public class ItemsServiceTest {

    @Test
    void shouldReturnItems() {
        //given service
        var service = new ItemsService();

        //when items are requested
        var items = service.getItems();

        //then expected items are returned
        assertEquals(createTestItems(), items);
    }

}
