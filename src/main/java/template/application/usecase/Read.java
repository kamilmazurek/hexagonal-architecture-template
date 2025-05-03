package template.application.usecase;

import template.application.domain.model.Item;

import java.util.List;
import java.util.Optional;

public interface Read {

    Optional<Item> read(Long id);

    List<Item> read();

}
