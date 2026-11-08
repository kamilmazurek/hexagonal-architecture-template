package template.infrastructure.adapter.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import template.application.domain.model.ItemId;

import static template.application.domain.model.ItemId.MAX_LENGTH;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ITEM")
public class ItemEntity {

    @Id
    @Column(length = MAX_LENGTH)
    private String id;

    private String name;

}
