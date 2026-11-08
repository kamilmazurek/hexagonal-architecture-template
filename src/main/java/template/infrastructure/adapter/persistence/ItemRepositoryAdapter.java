package template.infrastructure.adapter.persistence;

import com.google.common.annotations.VisibleForTesting;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import template.application.domain.model.Item;
import template.application.domain.model.ItemId;
import template.application.exception.ItemAlreadyExistsException;
import template.application.port.ItemRepositoryPort;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ItemRepositoryAdapter implements ItemRepositoryPort {

    @PersistenceContext
    private final EntityManager entityManager;

    private final ItemRepository repository;

    private final ModelMapper mapper;

    @Override
    public Optional<Item> read(ItemId id) {
        return repository.findById(id.value()).map(this::toDomainObject);
    }

    @Override
    public List<Item> read() {
        return repository.findAll().stream().map(this::toDomainObject).toList();
    }

    @Override
    @Transactional
    public void create(Item item) {
        try {
            entityManager.persist(toEntity(item));
            entityManager.flush();
        } catch (EntityExistsException | ConstraintViolationException e) {
            throw new ItemAlreadyExistsException(item.getId());
        }
    }

    @Override
    @Transactional
    public void upsert(Item item) {
        repository.save(toEntity(item));
    }

    @Override
    public void delete(ItemId id) {
        repository.deleteById(id.value());
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
