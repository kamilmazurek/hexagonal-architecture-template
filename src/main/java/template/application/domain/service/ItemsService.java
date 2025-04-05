package template.application.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import template.application.domain.model.Item;
import template.application.port.ItemsRepositoryPort;
import template.application.port.ItemsWebPort;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemsService implements ItemsWebPort {

    private ItemsRepositoryPort port;

    @Override
    public List<Item> getItems() {
        return port.getItems();
    }

}
