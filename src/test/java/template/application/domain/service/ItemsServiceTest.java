package template.application.domain.service;

import org.junit.jupiter.api.Test;
import template.application.domain.model.Item;
import template.infrastructure.adapter.persistence.ItemsRepositoryAdapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

        //and adapter was involved in retrieving the data
        verify(adapter, times(1)).getItems();
    }

    @Test
    void shouldPutItem() {
        //given adapter
        var adapter = mock(ItemsRepositoryAdapter.class);

        //and service
        var service = new ItemsService(adapter);

        //and item
        var item = Item.builder().name("Item A").build();

        //when item is put
        service.putItem(1L, item);

        //and adapter was involved in saving the data
        verify(adapter, times(1)).putItem(1L, item);
    }

}
