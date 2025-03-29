package template.adapter.web;

import org.springframework.stereotype.Service;
import template.api.model.ItemDTO;

import java.util.List;

@Service
public class ItemsWebAdapter {

    public List<ItemDTO> getItems() {
        return List.of(
                new ItemDTO().id(1L).name("Item A"),
                new ItemDTO().id(2L).name("Item B"),
                new ItemDTO().id(3L).name("Item C")
        );
    }

}
