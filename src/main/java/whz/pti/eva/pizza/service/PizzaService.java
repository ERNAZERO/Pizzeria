package whz.pti.eva.pizza.service;

import org.springframework.data.domain.Page;
import whz.pti.eva.pizza.domain.dto.PizzaCreateDto;
import whz.pti.eva.pizza.domain.entity.Pizza;

import java.util.List;

public interface PizzaService {

    void addPizza(PizzaCreateDto pizzaCreateDto) throws Exception;

    void deletePizza(int pizzaId);

    void updatePizza(int pizzaId, PizzaCreateDto pizzaCreateDto);

    void archivePizza(int pizzaId);

    void restorePizza(int pizzaId);

    Pizza getPizzaById(int pizzaId);

    Page<Pizza> getAllPizzas(String search, String status, int page, int size);

    void recoverPizza(int pizzaId);

    List<Pizza> getLastFourPizzas();




}
