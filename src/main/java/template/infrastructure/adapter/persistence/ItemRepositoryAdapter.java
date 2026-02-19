package template.infrastructure.adapter.persistence;

import com.google.common.annotations.VisibleForTesting;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import template.application.domain.model.Item;
import template.application.port.ItemRepositoryPort;

import java.util.List;
import java.util.Optional;

import static template.infrastructure.adapter.persistence.Queries.ALTER_SEQUENCE_QUERY;
import static template.infrastructure.adapter.persistence.Queries.CURRENT_SEQ_VAL_QUERY;
import static template.infrastructure.adapter.persistence.Queries.LOCK_QUERY;
import static template.infrastructure.adapter.persistence.Queries.MERGE_QUERY;

@Component
@AllArgsConstructor
public class ItemRepositoryAdapter implements ItemRepositoryPort {

    @PersistenceContext
    private final EntityManager entityManager;

    private final ItemRepository repository;

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
    @Transactional
    public void create(Item item) {
        //Works with H2 for development and testing, but may need adjustments for production databases
        entityManager.createNativeQuery(LOCK_QUERY).getResultList();
        var itemEntity = toEntity(item);
        repository.save(itemEntity);
    }

    @Override
    @Transactional
    public void upsert(Long itemId, Item item) {
        //Works with H2 for development and testing, but may need adjustments for production databases
        entityManager.createNativeQuery(LOCK_QUERY).getResultList();
        entityManager.createNativeQuery(MERGE_QUERY).setParameter(1, itemId).setParameter(2, item.getName()).executeUpdate();
        syncSequence(itemId);
    }

    private void syncSequence(Long insertedId) {
        var currentSeqVal = (Number) entityManager.createNativeQuery(CURRENT_SEQ_VAL_QUERY).getSingleResult();

        if (insertedId < currentSeqVal.longValue()) {
            return;
        }

        entityManager.createNativeQuery(String.format(ALTER_SEQUENCE_QUERY,  insertedId + 1)).executeUpdate();
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
