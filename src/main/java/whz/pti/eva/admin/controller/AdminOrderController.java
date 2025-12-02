package whz.pti.eva.admin.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import whz.pti.eva.cart.service.impl.CartServiceImpl;

@RequestMapping("/admin/order")
@Controller
public class AdminOrderController {

    @Autowired
    private CartServiceImpl cartService;


    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", cartService.allOrders());
        return "admins/admin-orders";
    }

    @GetMapping("/{id}")
    public String getOrderById(@PathVariable int id, Model model) {
        model.addAttribute("order", cartService.getCartById(id));
        return "admins/admin-order-view";
    }

}
