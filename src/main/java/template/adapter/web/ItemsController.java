package template.adapter.web;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import template.api.ItemsApi;
import template.api.model.ItemDTO;

import java.util.List;

@RestController
@AllArgsConstructor
public class ItemsController implements ItemsApi {

    private ItemsWebAdapter adapter;

    @Override
    public ResponseEntity<List<ItemDTO>> getItems() {
        return ResponseEntity.ok(adapter.getItems());
    }

}
