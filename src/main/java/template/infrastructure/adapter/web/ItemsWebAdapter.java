package template.infrastructure.adapter.web;

import com.google.common.annotations.VisibleForTesting;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import template.api.model.ItemDTO;
import template.application.domain.model.Item;
import template.application.port.ItemsWebPort;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ItemsWebAdapter {

    private final ItemsWebPort port;

    private final ModelMapper mapper;

    public Optional<ItemDTO> getItem(Long id) {
        return port.read(id).map(this::toDTO);
    }

    public List<ItemDTO> getItems() {
        return port.read().stream().map(this::toDTO).toList();
    }

    public void postItem(ItemDTO itemDTO) {
        port.create(toDomainObject(itemDTO));
    }

    public void putItem(Long itemId, ItemDTO itemDTO) {
        port.upsert(itemId, toDomainObject(itemDTO));
    }

    public void deleteItem(Long itemId) {
        port.delete(itemId);
    }

    @VisibleForTesting
    ItemDTO toDTO(Item item) {
        return mapper.map(item, ItemDTO.class);
    }

    @VisibleForTesting
    Item toDomainObject(ItemDTO itemDTO) {
        return mapper.map(itemDTO, Item.class);
    }

}
