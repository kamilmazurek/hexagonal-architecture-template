package template.adapter.web;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static template.util.TestItems.createTestItemDTOs;

public class ItemsWebAdapterTest {

    @Test
    void shouldReturnItems() {
        //given adapter
        var adapter = new ItemsWebAdapter();

        //when items are requested
        var items = adapter.getItems();

        //then items are returned
        assertEquals(createTestItemDTOs(), items);
    }

}
