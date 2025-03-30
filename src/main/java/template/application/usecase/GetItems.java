package template.application.usecase;

import template.application.domain.model.Item;

import java.util.List;

@FunctionalInterface
public interface GetItems {

    List<Item> getItems();

}
