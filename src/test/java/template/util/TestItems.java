package template.util;

import template.api.model.ItemDTO;

import java.util.List;

public class TestItems {

    public static List<ItemDTO> createTestItemDTOs() {
        var itemA = new ItemDTO().id(1L).name("Item A");
        var itemB = new ItemDTO().id(2L).name("Item B");
        var itemC = new ItemDTO().id(3L).name("Item C");

        return List.of(itemA, itemB, itemC);
    }

}
