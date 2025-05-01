package template.application.usecase;

import template.application.domain.model.Item;

@FunctionalInterface
public interface PutItem {

    void putItem(Long itemId, Item item);

}
