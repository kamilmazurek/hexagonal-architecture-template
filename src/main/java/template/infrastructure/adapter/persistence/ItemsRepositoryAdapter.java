package template.infrastructure.adapter.persistence;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import template.infrastructure.adapter.persistence.model.ItemEntity;
import template.application.domain.model.Item;
import template.application.port.ItemsRepositoryPort;

import java.util.List;

@Component
@AllArgsConstructor
public class ItemsRepositoryAdapter implements ItemsRepositoryPort {

    private ItemsRepository repository;

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public List<Item> getItems() {
        return repository.findAll().stream().map(this::toDomainObject).toList();
    }

    private Item toDomainObject(ItemEntity itemEntity) {
        return mapper.map(itemEntity, Item.class);
    }

}
