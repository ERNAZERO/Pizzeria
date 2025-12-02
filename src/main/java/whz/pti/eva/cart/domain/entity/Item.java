package whz.pti.eva.cart.domain.entity;


import jakarta.persistence.*;
import whz.pti.eva.pizza.domain.entity.Pizza;
import whz.pti.eva.pizza.domain.entity.PizzaPrice;
import whz.pti.eva.pizza.domain.enums.PizzaSize;

import java.util.UUID;


@Entity
public class Item{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID itemId;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "pizza_id", referencedColumnName = "pizzaId", nullable = false)
    private Pizza pizza;
    @Enumerated(EnumType.STRING)
    private PizzaSize size;


    public Item(){
    }

    public Item(UUID itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }


    public double getTotalPrice() {
        PizzaPrice priceForSize = pizza.getPizzaPrices().stream()
                .filter(price -> price.getPizzaSize().equals(size))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Price not found for size: " + size));
        return priceForSize.getPrice() * quantity;
    }

    public double getCurrentPrice() {
        PizzaPrice priceForSize = pizza.getPizzaPrices().stream()
                .filter(price -> price.getPizzaSize().equals(size))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Price not found for size: " + size));
        return priceForSize.getPrice();
    }

    public UUID getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public PizzaSize getSize() {
        return size;
    }

    public void setSize(PizzaSize size) {
        this.size = size;
    }


}
