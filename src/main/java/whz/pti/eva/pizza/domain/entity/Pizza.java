package whz.pti.eva.pizza.domain.entity;

import jakarta.persistence.*;
import whz.pti.eva.pizza.domain.enums.PizzaStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pizzaId;
    private String name;
    @Enumerated(EnumType.STRING)
    private PizzaStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pizza_id")
    private List<PizzaPrice> pizzaPrices;
    private LocalDateTime createdDate;
    @Lob
    private String image;
    @Column(length = 2000)
    private String description;

    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

    public Pizza(){
    }

    public Pizza(String name, PizzaStatus status, List<PizzaPrice> pizzaPrices, LocalDateTime createdDate, String image, String description) {
        this.name = name;
        this.status = status;
        this.pizzaPrices = pizzaPrices;
        this.createdDate = createdDate;
        this.image = image;
        this.description = description;
    }

    public int getPizzaId() {
        return pizzaId;
    }

    public List<PizzaPrice> getPizzaPrices() {
        return pizzaPrices;
    }

    public void setPizzaPrices(List<PizzaPrice> pizzaPrices) {
        this.pizzaPrices = pizzaPrices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PizzaStatus getStatus() {
        return status;
    }

    public void setStatus(PizzaStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
