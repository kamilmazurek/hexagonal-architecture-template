package template.infrastructure.adapter.web;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import template.api.ItemsApi;
import template.api.model.ItemDTO;

import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
public class ItemController implements ItemsApi {

    private final ItemWebAdapter adapter;

    @Override
    public ResponseEntity<ItemDTO> getItem(String id) {
        return adapter.getItem(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<ItemDTO>> getItems() {
        return ResponseEntity.ok(adapter.getItems());
    }

    @Override
    public ResponseEntity<Void> postItem(ItemDTO itemDTO) {
        if (itemDTO.getId() != null) {
            return ResponseEntity.badRequest().build();
        }

        adapter.postItem(itemDTO);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> putItem(String itemId, ItemDTO itemDTO) {
        if (!hasValidId(itemId, itemDTO)) {
            return ResponseEntity.badRequest().build();
        }

        adapter.putItem(itemDTO);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteItem(String itemId) {
        if (adapter.getItem(itemId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        adapter.deleteItem(itemId);
        return ResponseEntity.ok().build();
    }

    private boolean hasValidId(String itemId, ItemDTO itemDTO) {
        return itemDTO.getId() != null && Objects.equals(itemId, itemDTO.getId());
    }

}
