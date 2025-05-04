package template.application.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import template.application.domain.model.Item;
import template.application.port.ItemsRepositoryPort;
import template.application.port.ItemsWebPort;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemsService implements ItemsWebPort {

    private ItemsRepositoryPort port;

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
        port.create(item);
    }

    @Override
    public void insert(Long itemId, Item item) {
        port.insert(itemId, item);
    }

    @Override
    public void delete(Long itemId) {
        port.delete(itemId);
    }
}
