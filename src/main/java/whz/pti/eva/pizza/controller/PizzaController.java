package whz.pti.eva.pizza.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import whz.pti.eva.components.CartUtility;
import whz.pti.eva.pizza.domain.dto.PizzaToCartDto;
import whz.pti.eva.pizza.domain.entity.Pizza;
import whz.pti.eva.pizza.domain.enums.PizzaStatus;
import whz.pti.eva.pizza.service.PizzaService;

@Controller
@RequestMapping("/")
public class PizzaController {

    @Autowired
    private PizzaService pizzaService;

    @Autowired
    private CartUtility cartUtility;


    @GetMapping("/")
    public String getMainPage(Model model, HttpSession session, HttpServletRequest request) {
        cartUtility.initializeCart(model, session, request);
        model.addAttribute("pizzas", pizzaService.getLastFourPizzas());
        return "main";
    }

    @GetMapping("/catalog")
    public String getCatalogPage(Model model,
                                 @RequestParam(value = "search", required = false) String search,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "8") int size,
                                 HttpSession session,
                                 HttpServletRequest request) {
        cartUtility.initializeCart(model, session, request);
        Page<Pizza> pizzaPage = pizzaService.getAllPizzas(search, PizzaStatus.ACTIVE.name(), page, size);
        model.addAttribute("pizzas", pizzaPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pizzaPage.getTotalPages());
        model.addAttribute("totalItems", pizzaPage.getTotalElements());
        return "catalog";
    }

    @GetMapping("/catalog/{id}")
    public String getPizzaPage(Model model, @PathVariable int id, HttpSession session, HttpServletRequest request) {
        cartUtility.initializeCart(model, session, request);
        Pizza pizza = pizzaService.getPizzaById(id);
        if (pizza == null) {
            return "/error/404";
        }
        model.addAttribute("pizza", pizzaService.getPizzaById(id));
        model.addAttribute("pizzaToCartDto", new PizzaToCartDto());
        return "catalog-item";
    }
}

