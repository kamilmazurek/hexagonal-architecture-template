package template.infrastructure.adapter.persistence;

import com.google.common.annotations.VisibleForTesting;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import template.application.domain.model.Item;
import template.application.port.ItemsRepositoryPort;
import template.infrastructure.adapter.persistence.model.ItemEntity;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ItemsRepositoryAdapter implements ItemsRepositoryPort {

    private final ItemsRepository repository;

    private final ModelMapper mapper;

    @Override
    public Optional<Item> read(Long id) {
        return repository.findById(id).map(this::toDomainObject);
    }

    @Override
    public List<Item> read() {
        return repository.findAll().stream().map(this::toDomainObject).toList();
    }

    @Override
    public void create(Item item) {
        var itemEntity = toEntity(item);
        var maxID = Optional.ofNullable(repository.findMaxID()).orElse(0L);
        itemEntity.setId(maxID + 1);

        repository.save(itemEntity);
    }

    @Override
    public void upsert(Long itemId, Item item) {
        item.setId(itemId);
        repository.save(toEntity(item));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @VisibleForTesting
    Item toDomainObject(ItemEntity itemEntity) {
        return mapper.map(itemEntity, Item.class);
    }

    @VisibleForTesting
    ItemEntity toEntity(Item item) {
        return mapper.map(item, ItemEntity.class);
    }

}
