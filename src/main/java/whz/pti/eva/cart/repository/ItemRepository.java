package whz.pti.eva.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whz.pti.eva.cart.domain.entity.Item;

import java.util.UUID;


public interface ItemRepository extends JpaRepository<Item, UUID> {
}
