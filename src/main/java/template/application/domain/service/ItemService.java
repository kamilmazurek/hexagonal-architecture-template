package template.application.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import template.application.domain.model.Item;
import template.application.domain.model.ItemId;
import template.application.domain.util.ItemIdGenerator;
import template.application.exception.ItemIdAlreadySetException;
import template.application.exception.ItemIdMismatchException;
import template.application.port.ItemRepositoryPort;
import template.application.port.ItemWebPort;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemService implements ItemWebPort {

    private final ItemRepositoryPort port;

    @Override
    public Optional<Item> read(ItemId id) {
        return port.read(id);
    }

    @Override
    public List<Item> read() {
        return port.read();
    }

    @Override
    public void create(Item item) {
        if (item.getId() != null) {
            throw new ItemIdAlreadySetException(item.getId());
        }

        port.create(item.toBuilder().id(ItemIdGenerator.fromName(item.getName())).build());
    }

    @Override
    public void upsert(Item item) {
        var expectedId = ItemIdGenerator.fromName(item.getName());

        if (!expectedId.equals(item.getId())) {
            throw new ItemIdMismatchException(expectedId, item.getId());
        }

        port.upsert(item);
    }

    @Override
    public void delete(ItemId itemId) {
        port.delete(itemId);
    }

}
