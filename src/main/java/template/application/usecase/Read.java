package template.application.usecase;

import template.application.domain.model.Item;
import template.application.domain.model.ItemId;

import java.util.List;
import java.util.Optional;

public interface Read {

    Optional<Item> read(ItemId id);

    List<Item> read();

}
