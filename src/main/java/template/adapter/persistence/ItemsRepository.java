package template.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import template.adapter.persistence.model.ItemEntity;

@Repository
public interface ItemsRepository extends JpaRepository<ItemEntity, Long> {
}
