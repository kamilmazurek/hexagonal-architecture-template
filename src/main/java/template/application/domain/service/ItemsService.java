package template.application.domain.service;

import org.springframework.stereotype.Service;
import template.application.domain.model.Item;
import template.application.port.ItemsPort;

import java.util.List;

@Service
public class ItemsService implements ItemsPort {

    @Override
    public List<Item> getItems() {
        return List.of(
                Item.builder().id(1L).name("Item A").build(),
                Item.builder().id(2L).name("Item B").build(),
                Item.builder().id(3L).name("Item C").build()
        );
    }

}
