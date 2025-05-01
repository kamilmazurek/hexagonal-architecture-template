package template.infrastructure.adapter.persistence;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import template.application.domain.model.Item;
import template.application.port.ItemsRepositoryPort;
import template.infrastructure.adapter.persistence.model.ItemEntity;

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

    @Override
    public void putItem(Long itemId, Item item) {
        item.setId(itemId);
        repository.save(toEntity(item));
    }

    private Item toDomainObject(ItemEntity itemEntity) {
        return mapper.map(itemEntity, Item.class);
    }

    private ItemEntity toEntity(Item item) {
        return mapper.map(item, ItemEntity.class);
    }

}
