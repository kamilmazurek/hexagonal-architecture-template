package template.application.usecase;

import template.application.domain.model.Item;

@FunctionalInterface
public interface InsertItem {

    void insertItem(Long itemId, Item item);

}
