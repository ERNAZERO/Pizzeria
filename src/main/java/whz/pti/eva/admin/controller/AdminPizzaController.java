package whz.pti.eva.admin.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import whz.pti.eva.pizza.domain.dto.PizzaCreateDto;
import whz.pti.eva.pizza.domain.entity.Pizza;
import whz.pti.eva.pizza.domain.enums.PizzaStatus;
import whz.pti.eva.pizza.service.impl.PizzaServiceImpl;

import java.io.IOException;

@Controller
@RequestMapping ("/admin")
public class AdminPizzaController {

    private static final Logger log = LoggerFactory.getLogger(AdminPizzaController.class);

    @Autowired
    private PizzaServiceImpl pizzaService;


    @GetMapping
    public String listPizzas(Model model,
                             @RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "search", required = false) String search,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size
                             ) {
        Page<Pizza> pizzaPage = pizzaService.getAllPizzas(search, status, page, size);
        model.addAttribute("pizzas", pizzaPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pizzaPage.getTotalPages());
        model.addAttribute("totalItems", pizzaPage.getTotalElements());
        model.addAttribute("statuses", PizzaStatus.values());
        model.addAttribute("search", search);
        model.addAttribute("status", status);
        return "admins/admin-main";
    }

    // Show form to add a new pizza
    @GetMapping("/add")
    public String showAddPizzaForm(Model model) {
        model.addAttribute("pizza", new PizzaCreateDto());
        return "admins/admin-pizza-add";
    }

    // Handle form submission for adding a pizza
    @PostMapping("/add")
    public String addPizza(@ModelAttribute PizzaCreateDto pizzaCreateDto) throws IOException {
        pizzaService.addPizza(pizzaCreateDto);
        return "redirect:/admin";
    }


    @GetMapping("pizza/{id}")
    public String getPizzaById(@PathVariable int id, Model model) {
        model.addAttribute("pizza", pizzaService.getPizzaById(id));
        model.addAttribute("statuses", PizzaStatus.values());
        return "admins/admin-pizza-view";
    }

    // Show form to edit a pizza
    @GetMapping("pizza/edit/{id}")
    public String showEditPizzaForm(@PathVariable("id") int id, Model model) {
        Pizza oldPizza = pizzaService.getPizzaById(id);
        PizzaCreateDto newPizza = new PizzaCreateDto();
        newPizza.setName(oldPizza.getName());
        newPizza.setDescription(oldPizza.getDescription());
        newPizza.setSmallPrice(oldPizza.getPizzaPrices().get(0).getPrice());
        newPizza.setMediumPrice(oldPizza.getPizzaPrices().get(1).getPrice());
        newPizza.setLargePrice(oldPizza.getPizzaPrices().get(2).getPrice());
        model.addAttribute("oldPizza", oldPizza);
        model.addAttribute("newPizza", newPizza);
        return "admins/admin-pizza-edit";
    }

    // Handle form submission for updating a pizza
    @PostMapping("/pizza/edit/{id}")
    public String updatePizza(@PathVariable int id, @ModelAttribute PizzaCreateDto updatePizza) {
        pizzaService.updatePizza(id, updatePizza);
        return "redirect:/admin";
    }

    // Archive a pizza
    @PostMapping("/pizza/archive/{id}")
    public String archivePizza(@PathVariable int id) {
        pizzaService.archivePizza(id);
        return "redirect:/admin";
    }

    // Restore a pizza
    @PostMapping("/pizza/restore/{id}")
    public String restorePizza(@PathVariable int id) {
        pizzaService.restorePizza(id);
        return "redirect:/admin";
    }

    // Delete a pizza permanently
    @PostMapping("/pizza/delete/{id}")
    public String deletePizza(@PathVariable int id) {
        pizzaService.deletePizza(id);
        return "redirect:/admin";
    }

    @PostMapping("/pizza/recover/{id}")
    public String recoverPizza(@PathVariable int id) {
        pizzaService.recoverPizza(id);
        return "redirect:/admin";
    }


}
