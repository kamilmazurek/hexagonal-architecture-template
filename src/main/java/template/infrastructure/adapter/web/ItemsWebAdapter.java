package template.infrastructure.adapter.web;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import template.api.model.ItemDTO;
import template.application.domain.model.Item;
import template.application.port.ItemsWebPort;

import java.util.List;

@Component
@AllArgsConstructor
public class ItemsWebAdapter {

    private ItemsWebPort port;

    private final ModelMapper mapper = new ModelMapper();

    public List<ItemDTO> getItems() {
        return port.getItems().stream().map(this::toDTO).toList();
    }

    public void putItem(Long itemId, ItemDTO itemDTO) {
        port.putItem(itemId, toDomainObject(itemDTO));
    }

    private ItemDTO toDTO(Item item) {
        return mapper.map(item, ItemDTO.class);
    }

    private Item toDomainObject(ItemDTO itemDTO) {
        return mapper.map(itemDTO, Item.class);
    }

}
