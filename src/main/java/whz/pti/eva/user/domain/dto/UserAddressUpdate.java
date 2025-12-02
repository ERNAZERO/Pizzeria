package whz.pti.eva.user.domain.dto;

public class UserAddressUpdate {
    private String town;
    private String street;
    private String houseNumber;
    private String postalCode;

    public UserAddressUpdate(String town, String street, String houseNumber, String postalCode) {
        this.town = town;
        this.street = street;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
    }

    public UserAddressUpdate() {
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
