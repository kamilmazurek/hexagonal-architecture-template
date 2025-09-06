package template.application.usecase;

import template.application.domain.model.Item;

public interface Upsert {

    void upsert(Long itemId, Item item);

}
