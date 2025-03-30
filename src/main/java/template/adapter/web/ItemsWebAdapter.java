package template.adapter.web;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import template.api.model.ItemDTO;
import template.application.domain.model.Item;
import template.application.domain.service.ItemsService;

import java.util.List;

@Component
@AllArgsConstructor
public class ItemsWebAdapter {

    private ItemsService service;

    private final ModelMapper mapper = new ModelMapper();

    public List<ItemDTO> getItems() {
        return service.getItems().stream().map(this::toDTO).toList();
    }

    private ItemDTO toDTO(Item item) {
        return mapper.map(item, ItemDTO.class);
    }

}
