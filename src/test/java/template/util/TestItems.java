package template.util;

import template.infrastructure.adapter.persistence.ItemEntity;
import template.api.model.ItemDTO;
import template.application.domain.model.Item;
import template.application.domain.model.ItemId;

import java.util.List;

public class TestItems {

    public static List<ItemDTO> createTestItemDTOs() {
        var itemA = new ItemDTO().id("item-a").name("Item A");
        var itemB = new ItemDTO().id("item-b").name("Item B");
        var itemC = new ItemDTO().id("item-c").name("Item C");

        return List.of(itemA, itemB, itemC);
    }

    public static List<Item> createTestItems() {
        var itemA = Item.builder().id(new ItemId("item-a")).name("Item A").build();
        var itemB = Item.builder().id(new ItemId("item-b")).name("Item B").build();
        var itemC = Item.builder().id(new ItemId("item-c")).name("Item C").build();

        return List.of(itemA, itemB, itemC);
    }

    public static List<ItemEntity> createTestItemEntities() {
        var itemA = ItemEntity.builder().id("item-a").name("Item A").build();
        var itemB = ItemEntity.builder().id("item-b").name("Item B").build();
        var itemC = ItemEntity.builder().id("item-c").name("Item C").build();

        return List.of(itemA, itemB, itemC);
    }

}
