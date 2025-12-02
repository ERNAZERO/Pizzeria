package whz.pti.eva.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whz.pti.eva.cart.domain.entity.Cart;
import whz.pti.eva.cart.domain.enums.CartStatus;
import whz.pti.eva.user.domain.entity.Users;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUsersAndCartStatus(Users users, CartStatus cartStatus);

    List<Cart> findAllByUsersAndCartStatus(Users users, CartStatus cartStatus);

    List<Cart> findCartsByCartStatus(CartStatus cartStatus);

}
