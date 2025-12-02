package whz.pti.eva.cart.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import whz.pti.eva.cart.domain.entity.Cart;
import whz.pti.eva.cart.service.CartService;
import whz.pti.eva.components.AuthUtility;
import whz.pti.eva.components.CartUtility;
import whz.pti.eva.pizza.domain.dto.PizzaToCartDto;
import whz.pti.eva.pizza.domain.enums.PizzaSize;
import whz.pti.eva.user.domain.entity.Users;
import whz.pti.eva.user.service.impl.UserServiceImpl;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartUtility cartUtility;

    @Autowired
    private AuthUtility authUtility;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public String getCartPage(Model model, HttpSession session) {
        Cart cart = cartUtility.getCart(session);
        Users user = authUtility.getAuthenticatedUser();
        if(user != null) {
            model.addAttribute("userAddress", userService.getUserAddresses(authUtility.getAuthenticatedUserId()));
            if(cart.getDeliveryAddress() != null) {
                model.addAttribute("selectedAddress", cart.getDeliveryAddress().getDeliveryAddressId());
            }
            else{
                model.addAttribute("selectedAddress", null);
            }

        }else{
            model.addAttribute("userAddress", null);
            model.addAttribute("selectedAddress", null);
        }
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cart.calculateTotalPrice());
        model.addAttribute("pizzaSizes", PizzaSize.values());
        return "cart";
    }

    @PostMapping("/add")
    public String addPizzaToCart(@ModelAttribute PizzaToCartDto pizzaToCartDto, Model model, HttpSession session) {
        Cart cart = cartUtility.getCart(session);
        Users user = authUtility.getAuthenticatedUser();
        if (user != null) {
            cartService.addPizzaToCart(pizzaToCartDto, String.valueOf(user.getUserId()), cart.getCartId());
        } else {
            cartService.addPizzaToCart(pizzaToCartDto, null, cart.getCartId());
        }
        model.addAttribute("cartQuantity", cart.getItems().size());
        return "redirect:/catalog";
    }

    @PostMapping("/updateQuantity")
    public String updateCartItemQuantity(@RequestParam("itemId") String itemId, @RequestParam("action") String action) {
        cartService.updateItemQuantity(itemId, action);
        return "redirect:/cart";
    }

    @PostMapping("/updateSize")
    public String updatePizzaSize(@RequestParam("itemId") String itemId, @RequestParam("size") String size, HttpSession session) {
        String cartId = cartUtility.getCartIdFromSession(session);
        cartService.updatePizzaSize(itemId, size, cartId);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeItemFromCart(@RequestParam("itemId") String itemId, HttpSession session) {
        String cartId = cartUtility.getCartIdFromSession(session);
        cartService.removeItemFromCart(itemId, cartId);
        return "redirect:/cart";
    }

    @PostMapping("/order/{id}")
    public String makeOrder(HttpSession session, @PathVariable int id, HttpServletResponse response) {
        if (authUtility.isAuthenticated()) {
            cartService.makeOrder(id);
            session.removeAttribute("cartId");
            return "redirect:/customer/orderMaking";
        } else {
            cartUtility.storeCartInCookie(id, response);
            session.removeAttribute("cartId");
            return "redirect:/login";
        }
    }

    @PostMapping("/addDeliveryAddress")
    public String addDeliveryAddress(@RequestParam("addressId") Long addressId, @RequestParam("cartId") int cartId) {
        cartService.addDeliveryAddress(cartId, addressId);
        return "redirect:/cart";
    }
}
