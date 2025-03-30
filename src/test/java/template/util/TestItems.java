package template.util;

import template.api.model.ItemDTO;
import template.application.domain.model.Item;

import java.util.List;

public class TestItems {

    public static List<ItemDTO> createTestItemDTOs() {
        var itemA = new ItemDTO().id(1L).name("Item A");
        var itemB = new ItemDTO().id(2L).name("Item B");
        var itemC = new ItemDTO().id(3L).name("Item C");

        return List.of(itemA, itemB, itemC);
    }

    public static List<Item> createTestItems() {
        var itemA = Item.builder().id(1L).name("Item A").build();
        var itemB = Item.builder().id(2L).name("Item B").build();
        var itemC = Item.builder().id(3L).name("Item C").build();

        return List.of(itemA, itemB, itemC);
    }

}
