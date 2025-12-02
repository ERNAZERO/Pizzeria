package whz.pti.eva.pizza.domain.entity;


import jakarta.persistence.*;
import whz.pti.eva.pizza.domain.enums.PizzaSize;

@Entity
public class PizzaPrice {


    public PizzaPrice(PizzaSize pizzaSize, double price) {
        this.pizzaSize = pizzaSize;
        this.price = price;
    }

    public PizzaPrice() {
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pizzaPriceId;
    @Enumerated(EnumType.STRING)
    private PizzaSize pizzaSize;
    private double price;


    public int getPizzaPriceId() {
        return pizzaPriceId;
    }

    public void setPizzaPriceId(int pizzaPriceId) {
        this.pizzaPriceId = pizzaPriceId;
    }

    public PizzaSize getPizzaSize() {
        return pizzaSize;
    }

    public void setPizzaSize(PizzaSize pizzaSize) {
        this.pizzaSize = pizzaSize;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
