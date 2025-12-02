package whz.pti.eva.user.domain.dto;

public class UserRegistrationDto {

    private String email;
    private String firstName;
    private String lastName;
    private String passwordHash;
    private String passwordHashConfirm;
    private String street;
    private String houseNumber;
    private String town;
    private String postalCode;
    private String accType;

    public UserRegistrationDto() {
    }

    public UserRegistrationDto(String email, String firstName, String lastName, String passwordHash, String passwordHashConfirm, String street, String houseNumber, String town, String postalCode, String accType) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.passwordHashConfirm = passwordHashConfirm;
        this.street = street;
        this.houseNumber = houseNumber;
        this.town = town;
        this.postalCode = postalCode;
        this.accType = accType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    public String getPasswordHashConfirm() {
        return passwordHashConfirm;
    }

    public void setPasswordHashConfirm(String passwordHashConfirm) {
        this.passwordHashConfirm = passwordHashConfirm;
    }

    @Override
    public String toString() {
        return "UserRegistrationDto{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", passwordHashConfirm='" + passwordHashConfirm + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", town='" + town + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", accType='" + accType + '\'' +
                '}';
    }
}
