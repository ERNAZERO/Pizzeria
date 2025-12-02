package whz.pti.eva.pizza.domain.dto;

import org.springframework.web.multipart.MultipartFile;
import whz.pti.eva.pizza.domain.entity.PizzaPrice;

import java.util.List;

public class PizzaCreateDto {


    private String name;
    private String description;
    private MultipartFile image;
    private List<PizzaPrice> pizzaPrices;
    private double smallPrice;
    private double mediumPrice;
    private double largePrice;


    public PizzaCreateDto() {
    }

    public PizzaCreateDto(String name, String description, MultipartFile image, List<PizzaPrice> pizzaPrices) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.pizzaPrices = pizzaPrices;
    }

    public double getSmallPrice() {
        return smallPrice;
    }

    public void setSmallPrice(double smallPrice) {
        this.smallPrice = smallPrice;
    }

    public double getMediumPrice() {
        return mediumPrice;
    }

    public void setMediumPrice(double mediumPrice) {
        this.mediumPrice = mediumPrice;
    }

    public double getLargePrice() {
        return largePrice;
    }

    public void setLargePrice(double largePrice) {
        this.largePrice = largePrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public List<PizzaPrice> getPizzaPrices() {
        return pizzaPrices;
    }

    public void setPizzaPrices(List<PizzaPrice> pizzaPrices) {
        this.pizzaPrices = pizzaPrices;
    }
}
