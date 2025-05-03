package template.infrastructure.adapter.persistence;

import com.google.common.annotations.VisibleForTesting;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import template.application.domain.model.Item;
import template.application.port.ItemsRepositoryPort;
import template.infrastructure.adapter.persistence.model.ItemEntity;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Component
@AllArgsConstructor
public class ItemsRepositoryAdapter implements ItemsRepositoryPort {

    private ItemsRepository repository;

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public Optional<Item> read(Long id) {
        return repository.findById(id).map(this::toDomainObject);
    }

    @Override
    public List<Item> read() {
        return repository.findAll().stream().map(this::toDomainObject).toList();
    }

    @Override
    @Transactional
    public void create(Item item) {
        if (item.getId() != null) {
            var message = format("ID should be null, so it will be set by adapter, but it has been already set to %s instead", item.getId());
            throw new IllegalArgumentException(message);
        }
        var itemEntity = toEntity(item);
        var maxID = repository.findMaxID();
        itemEntity.setId(maxID + 1);
        repository.save(itemEntity);
    }

    @Override
    public void insert(Long itemId, Item item) {
        item.setId(itemId);
        repository.save(toEntity(item));
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
