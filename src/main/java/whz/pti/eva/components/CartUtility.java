package whz.pti.eva.components;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import whz.pti.eva.cart.domain.entity.Cart;
import whz.pti.eva.user.domain.entity.Users;
import whz.pti.eva.cart.service.CartService;

@Component
public class CartUtility {

    @Autowired
    private CartService cartService;

    @Autowired
    private AuthUtility authUtility;

    public void initializeCart(Model model, HttpSession session, HttpServletRequest request) {
        if (authUtility.isAuthenticated()) {
            Users user = authUtility.getAuthenticatedUser();
            unionCartWithCookies(request, user);
            Cart cart = cartService.getCurrentCartForUser(user);
            model.addAttribute("cartId", cart.getCartId());
            model.addAttribute("cartQuantity", cart.getItems() == null ? 0 : cart.getItems().size());
            session.setAttribute("cartId", cart.getCartId());
        } else {
            Cart cart = getCart(session);
            model.addAttribute("cartId", cart.getCartId());
            model.addAttribute("cartQuantity", cart.getItems().size());
        }
    }

    public Cart getCart(HttpSession session) {
        String cartId = getCartIdFromSession(session);
        if (cartId != null) {
            return cartService.getCartById(Integer.parseInt(cartId));
        } else {
            Cart cart = cartService.createCartWithoutUser();
            session.setAttribute("cartId", cart.getCartId());
            return cart;
        }
    }

    public String getCartIdFromSession(HttpSession session) {
        Object cartId = session.getAttribute("cartId");
        return cartId != null ? cartId.toString() : null;
    }

    public void unionCartWithCookies(HttpServletRequest request, Users user) {
        if (request != null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("cart".equalsIgnoreCase(cookie.getName())) {
                    cartService.unionCarts(Integer.parseInt(cookie.getValue()), user);
                    cookie.setMaxAge(0);
                }
            }
        }
    }

    public void storeCartInCookie(int cartId, HttpServletResponse response) {
        Cookie cookie = new Cookie("cart", String.valueOf(cartId));
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}

