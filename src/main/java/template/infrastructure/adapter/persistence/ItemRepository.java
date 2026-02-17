package template.infrastructure.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import template.infrastructure.adapter.persistence.model.ItemEntity;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    @Query("select max(item.id) from ItemEntity item")
    Long findMaxID();

}
