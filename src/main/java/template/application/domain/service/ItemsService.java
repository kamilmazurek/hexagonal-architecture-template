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
    public Optional<Item> getItem(Long id) {
        return port.getItem(id);
    }

    @Override
    public List<Item> getItems() {
        return port.getItems();
    }

    @Override
    public void putItem(Long itemId, Item item) {
        port.putItem(itemId, item);
    }

}
