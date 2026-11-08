package template.infrastructure.adapter.web;

import com.google.common.annotations.VisibleForTesting;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import template.api.model.ItemDTO;
import template.application.domain.model.Item;
import template.application.domain.model.ItemId;
import template.application.port.ItemWebPort;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ItemWebAdapter {

    private final ItemWebPort port;

    private final ModelMapper mapper;

    public Optional<ItemDTO> getItem(String id) {
        return Optional.ofNullable(id)
                .filter(ItemId::isValid)
                .map(ItemId::new)
                .flatMap(port::read)
                .map(this::toDTO);
    }

    public List<ItemDTO> getItems() {
        return port.read().stream().map(this::toDTO).toList();
    }

    public void postItem(ItemDTO itemDTO) {
        port.create(toDomainObject(itemDTO));
    }

    public void putItem(ItemDTO itemDTO) {
        port.upsert(toDomainObject(itemDTO));
    }

    public void deleteItem(String itemId) {
        port.delete(new ItemId(itemId));
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
