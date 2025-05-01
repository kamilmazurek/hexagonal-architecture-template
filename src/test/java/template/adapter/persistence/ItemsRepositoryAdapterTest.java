package template.adapter.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import template.application.domain.model.Item;
import template.infrastructure.adapter.persistence.ItemsRepository;
import template.infrastructure.adapter.persistence.ItemsRepositoryAdapter;
import template.infrastructure.adapter.persistence.model.ItemEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.invokeMethod;
import static template.util.TestItems.createTestItemEntities;
import static template.util.TestItems.createTestItems;

public class ItemsRepositoryAdapterTest {

    @Test
    void shouldReturnItems() {
        //given repository
        var repository = mock(ItemsRepository.class);
        when(repository.findAll()).thenReturn(createTestItemEntities());

        //and adapter
        var adapter = new ItemsRepositoryAdapter(repository);

        //when items are requested
        var items = adapter.getItems();

        //then expected items are returned
        assertEquals(createTestItems(), items);

        //and repository was queried for data
        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldPutItem() {
        //given repository
        var repository = mock(ItemsRepository.class);

        //and adapter
        var adapter = new ItemsRepositoryAdapter(repository);

        //and item
        var item = Item.builder().name("Item A").build();

        //when item is put
        adapter.putItem(1L, item);

        //then item has been put to repository
        verify(repository, times(1)).save(invokeMethod(adapter, "toEntity", item));
    }

}
