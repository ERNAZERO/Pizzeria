package whz.pti.eva.cart.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whz.pti.eva.cart.repository.ItemRepository;
import whz.pti.eva.cart.domain.entity.Cart;
import whz.pti.eva.cart.domain.entity.Item;
import whz.pti.eva.cart.domain.enums.CartStatus;
import whz.pti.eva.cart.repository.CartRepository;
import whz.pti.eva.cart.service.CartService;
import whz.pti.eva.pizza.domain.dto.PizzaToCartDto;
import whz.pti.eva.user.domain.entity.DeliveryAddress;
import whz.pti.eva.user.domain.entity.Users;
import whz.pti.eva.pizza.domain.enums.PizzaSize;
import whz.pti.eva.pizza.repository.PizzaRepository;
import whz.pti.eva.user.repository.DeliveryAddressRepository;
import whz.pti.eva.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private DeliveryAddressRepository deliveryAddressRepository;

    @Override
    public Cart getCurrentCartForUser(Users user) {
        Optional<Cart> optionalCart = cartRepository.findByUsersAndCartStatus(user, CartStatus.CURRENT);
        if(optionalCart.isPresent()){
            return optionalCart.get();
        }else{
            Cart cart = createCartWithoutUser();
            cart.setUsers(user);
            return cartRepository.save(cart);
        }
    }

    @Override
    public Cart createCartWithoutUser() {
        Cart newCart = new Cart();
        newCart.setCartStatus(CartStatus.CURRENT);
        newCart.setCreatedDate(LocalDateTime.now());
        newCart.setItems(new ArrayList<>());
        cartRepository.save(newCart);
        return newCart;
    }

    @Override
    public void makeOrder(int cartId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            if(cart.getDeliveryAddress() == null){
                cart.setDeliveryAddress(cart.getUsers().getDeliveryAddress().getFirst());
            }
            cart.setCartStatus(CartStatus.ORDERED);
            cart.setOrderedDate(LocalDateTime.now());
            cartRepository.save(cart);
        }
    }

    @Override
    public List<Cart> allOrdersByUser(Users user) {
        return cartRepository.findAllByUsersAndCartStatus(user, CartStatus.ORDERED);
    }

    @Override
    public List<Cart> allOrders() {
        return cartRepository.findCartsByCartStatus(CartStatus.ORDERED);
    }

    @Override
    public void addPizzaToCart(PizzaToCartDto pizzaToCartDto, String userId, int cartId) {
        Cart cart;
        if(userId != null){
            Optional<Users> user = userRepository.findById(UUID.fromString(userId));
            if (user.isPresent()) {
                cart = getCurrentCartForUser(user.get());
            }else{
                throw new RuntimeException("User not found");
            }
        } else{
            cart = getCartById(cartId);
        }


        Optional<Item> existingItem = cart.getItems().stream()
                .filter(item -> item.getPizza().getPizzaId() == pizzaToCartDto.getPizzaId() &&
                        item.getSize().equals(PizzaSize.valueOf(pizzaToCartDto.getSize())))
                .findFirst();

        if (existingItem.isPresent()) {
            Item item = existingItem.get();
            item.setQuantity(item.getQuantity() + pizzaToCartDto.getQuantity());
            itemRepository.save(item);
        } else {
            Item newItem = new Item();
            newItem.setPizza(pizzaRepository.findById(pizzaToCartDto.getPizzaId()).orElseThrow());
            newItem.setQuantity(pizzaToCartDto.getQuantity());
            newItem.setSize(PizzaSize.valueOf(pizzaToCartDto.getSize()));
            cart.getItems().add(itemRepository.save(newItem));
        }

        // Save the updated cart
        cartRepository.save(cart);
    }

    @Override
    public Cart getCartById(int cartId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        return optionalCart.orElse(null);
    }

    @Override
    public void updateItemQuantity(String itemId, String action) {
        Optional<Item> optionalItem = itemRepository.findById(UUID.fromString(itemId));
        if (optionalItem.isPresent()) {
            log.info("Updating item quantity: itemId = {}, action = {}", itemId, action);
            Item item = optionalItem.get();
            if (action.equals("increment")) {
                if(item.getQuantity() < 10 && item.getQuantity() > 0){
                    item.setQuantity(item.getQuantity() + 1);
                }
            } else if (action.equals("decrement")) {
                if(item.getQuantity() > 1){
                    item.setQuantity(item.getQuantity() - 1);
                }
            }
            itemRepository.save(item);
        }
    }

    @Override
    @Transactional
    public void updatePizzaSize(String itemId, String size, String cartId) {
        Optional<Item> optionalItem = itemRepository.findById(UUID.fromString(itemId));
        Optional<Cart> optionalCart = cartRepository.findById(Integer.parseInt(cartId));
        if (optionalItem.isPresent() && optionalCart.isPresent()) {
            Item item = optionalItem.get();
            Optional<Item> existPizza = optionalCart.get().getItems().stream()
                    .filter(i -> i.getPizza().equals(item.getPizza()) && i.getSize() == PizzaSize.valueOf(size))
                    .findFirst();
            if(existPizza.isEmpty()){
                item.setSize(PizzaSize.valueOf(size));
                itemRepository.save(item);
            } else{
                existPizza.get().setQuantity(existPizza.get().getQuantity() + item.getQuantity());
                removeItemFromCart(itemId, cartId);
                itemRepository.save(existPizza.get());
            }
        }
    }

    @Override
    @Transactional
    public void removeItemFromCart(String itemId, String cartId) {
        Cart cart = getCartById(Integer.parseInt(cartId));
        Item item = itemRepository.findById(UUID.fromString(itemId)).orElseThrow();
        cart.getItems().remove(item);
        cartRepository.save(cart);
        itemRepository.delete(item);
    }

    @Override
    @Transactional
    public void unionCarts(int cart, Users user) {
        log.info("Union carts: cart = {}, user = {}", cart, user);
        Optional<Cart> optionalCart = cartRepository.findById(cart);
        log.info("Cart is present {}", optionalCart.isPresent());
        if (optionalCart.isPresent()) {
            Cart currentCart = getCurrentCartForUser(user);
            log.info("Items in optional cart {}", optionalCart.get().toString());
            currentCart.getItems().addAll(optionalCart.get().getItems());
            cartRepository.save(currentCart);
            deleteCart(cart);
        }
    }

    @Override
    @Transactional
    public void deleteCart(int cartId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        optionalCart.ifPresent(cart -> cartRepository.delete(cart));
    }


    @Override
    public void addDeliveryAddress(int cartId, Long addressId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            DeliveryAddress deliveryAddress = deliveryAddressRepository.findById(addressId).orElseThrow();
            Cart cart = optionalCart.get();
            cart.setDeliveryAddress(deliveryAddress);
            cartRepository.save(cart);
            log.info("Delivery address added to cart: cartId = {}, addressId = {}", cartId, addressId);
        }
    }


}
