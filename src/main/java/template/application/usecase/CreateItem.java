package template.application.usecase;

import template.application.domain.model.Item;

@FunctionalInterface
public interface CreateItem {

    void createItem(Item item);

}
