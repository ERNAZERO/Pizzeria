package whz.pti.eva.cart.service;

import whz.pti.eva.cart.domain.entity.Cart;
import whz.pti.eva.pizza.domain.dto.PizzaToCartDto;
import whz.pti.eva.user.domain.entity.Users;

import java.util.List;

public interface CartService {

    Cart getCurrentCartForUser(Users user);

    Cart createCartWithoutUser();

    void makeOrder(int cartId);

    List<Cart> allOrdersByUser(Users user);

    List<Cart> allOrders();

    void addPizzaToCart(PizzaToCartDto pizzaToCartDto, String userId, int cartId);

    Cart getCartById(int cartId);

    void updateItemQuantity(String itemId, String action);

    void updatePizzaSize(String itemId, String size, String cartId);

    void removeItemFromCart(String itemId, String cartId);

    void unionCarts(int cart, Users user);

    void deleteCart(int cartId);

    void addDeliveryAddress(int cartId, Long addressId);

}
