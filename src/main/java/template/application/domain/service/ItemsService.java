package template.application.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import template.application.domain.model.Item;
import template.application.exception.ItemIdAlreadySetException;
import template.application.port.ItemsRepositoryPort;
import template.application.port.ItemsWebPort;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemsService implements ItemsWebPort {

    private final ItemsRepositoryPort port;

    @Override
    public Optional<Item> read(Long id) {
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

        port.create(item);
    }

    @Override
    public void upsert(Long itemId, Item item) {
        port.upsert(itemId, item);
    }

    @Override
    public void delete(Long itemId) {
        port.delete(itemId);
    }

}
