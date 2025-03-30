package template.adapter.web;

import org.junit.jupiter.api.Test;
import template.application.domain.service.ItemsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static template.util.TestItems.createTestItemDTOs;
import static template.util.TestItems.createTestItems;

public class ItemsWebAdapterTest {

    @Test
    void shouldReturnItems() {
        //givan service
        var service = mock(ItemsService.class);
        when(service.getItems()).thenReturn(createTestItems());

        //given adapter
        var adapter = new ItemsWebAdapter(service);

        //when items are requested
        var items = adapter.getItems();

        //then expected items are returned
        assertEquals(createTestItemDTOs(), items);
    }

}
