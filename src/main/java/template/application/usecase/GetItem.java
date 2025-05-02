package template.application.usecase;

import template.application.domain.model.Item;

import java.util.Optional;

@FunctionalInterface
public interface GetItem {

    Optional<Item> getItem(Long id);

}
