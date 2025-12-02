package whz.pti.eva.user.domain.entity;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class DeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long deliveryAddressId;
    private String street;
    private String houseNumber;
    private String town;
    private String postalCode;
    @ManyToOne
    private Users user;

    public DeliveryAddress() {}

    public DeliveryAddress(Long deliveryAddressId, String street, String houseNumber, String town, String postalCode) {
        this.deliveryAddressId = deliveryAddressId;
        this.street = street;
        this.houseNumber = houseNumber;
        this.town = town;
        this.postalCode = postalCode;
    }


    public Long getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(Long deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


    @Override
    public String toString() {
        return town + ", " + street + " " + houseNumber + ", " + postalCode;
    }

    public String getStreetHouseNumber() {
        return street + " " + houseNumber;
    }
}

