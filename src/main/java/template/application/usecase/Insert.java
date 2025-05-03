package template.application.usecase;

import template.application.domain.model.Item;

public interface Insert {

    void insert(Long itemId, Item item);

}
