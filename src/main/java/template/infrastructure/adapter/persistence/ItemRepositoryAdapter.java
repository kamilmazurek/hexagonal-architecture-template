package template.infrastructure.adapter.persistence;

import com.google.common.annotations.VisibleForTesting;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import template.application.domain.model.Item;
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
        // Works with H2 for development and testing, but may require adjustments for production depending on the database
        entityManager.createNativeQuery("SELECT 1 FROM ITEM_SEQ_LOCK FOR UPDATE").getResultList();
        var itemEntity = toEntity(item);
        repository.save(itemEntity);
    }

    @Override
    @Transactional
    public void upsert(Long itemId, Item item) {
        // Works with H2 for development and testing, but may require adjustments for production depending on the database
        entityManager.createNativeQuery("SELECT 1 FROM ITEM_SEQ_LOCK FOR UPDATE").getResultList();
        var mergeQuery = "MERGE INTO item (id, name) KEY(id) VALUES (?, ?)";
        entityManager.createNativeQuery(mergeQuery).setParameter(1, itemId).setParameter(2, item.getName()).executeUpdate();
        syncSequence(itemId);
    }

    private void syncSequence(Long insertedId) {
        var currentSeqValQuery = "SELECT CAST(BASE_VALUE AS BIGINT) FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_NAME = 'ITEM_SEQ'";
        var currentSeqVal = (Number) entityManager.createNativeQuery(currentSeqValQuery).getSingleResult();

        if (insertedId < currentSeqVal.longValue()) {
            return;
        }

        long newStart = insertedId + 1;
        entityManager.createNativeQuery("ALTER SEQUENCE ITEM_SEQ RESTART WITH " + newStart).executeUpdate();
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
