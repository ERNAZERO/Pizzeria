package whz.pti.eva.cart.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import whz.pti.eva.cart.domain.enums.CartStatus;
import whz.pti.eva.user.domain.entity.DeliveryAddress;
import whz.pti.eva.user.domain.entity.Users;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cartId;
    @ManyToOne
    private Users users;
    @OneToMany
    private List<Item> items;
    @OneToOne
    private DeliveryAddress deliveryAddress;
    private CartStatus cartStatus;
    private LocalDateTime createdDate;
    private LocalDateTime orderedDate;
    public Cart() {}

    public Cart(List<Item> items, CartStatus cartStatus, LocalDateTime createdDate, LocalDateTime orderedDate) {
        this.items = items;
        this.cartStatus = cartStatus;
        this.createdDate = createdDate;
        this.orderedDate = orderedDate;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public CartStatus getCartStatus() {
        return cartStatus;
    }

    public void setCartStatus(CartStatus cartStatus) {
        this.cartStatus = cartStatus;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(LocalDateTime orderedDate) {
        this.orderedDate = orderedDate;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public int getCartId() {
        return cartId;
    }


    public double calculateTotalPrice() {
        double totalPrice = 0.0;
        for(Item item : items) {
            totalPrice += item.getTotalPrice();
        }
        return BigDecimal.valueOf(totalPrice)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", users=" + users +
                ", items=" + items +
                ", cartStatus=" + cartStatus +
                ", createdDate=" + createdDate +
                ", orderedDate=" + orderedDate +
                '}';
    }

    public String getFormattedDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return orderedDate.format(formatter);
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
