package whz.pti.eva.pizza.domain.dto;

public class PizzaToCartDto {

    private int pizzaId;
    private int quantity;
    private String size;

    public PizzaToCartDto(int pizzaId, int quantity, String size) {
        this.pizzaId = pizzaId;
        this.quantity = quantity;
        this.size = size;
    }

    public PizzaToCartDto() {
    }

    public int getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
