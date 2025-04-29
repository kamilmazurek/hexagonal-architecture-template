package template.application.domain.service;

import org.junit.jupiter.api.Test;
import template.infrastructure.adapter.persistence.ItemsRepositoryAdapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static template.util.TestItems.createTestItems;

public class ItemsServiceTest {

    @Test
    void shouldReturnItems() {
        //given adapter
        var adapter = mock(ItemsRepositoryAdapter.class);
        when(adapter.getItems()).thenReturn(createTestItems());

        //and service
        var service = new ItemsService(adapter);

        //when items are requested
        var items = service.getItems();

        //then expected items are returned
        assertEquals(createTestItems(), items);
    }

}
