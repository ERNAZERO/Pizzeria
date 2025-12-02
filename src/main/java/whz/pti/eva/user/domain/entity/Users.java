package whz.pti.eva.user.domain.entity;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import whz.pti.eva.user.domain.enums.AccountType;
import whz.pti.eva.user.domain.enums.UserStatus;

import java.util.*;

@Entity
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;
    @Column(unique = true)
    private String email;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private AccountType accType;
    private String passwordHash;
    @OneToMany
    private List<DeliveryAddress> deliveryAddress = new ArrayList<>();
    private UserStatus userStatus;

    public Users() {}

    public Users(String email, String firstName, String lastName, AccountType accType, String passwordHash, List<DeliveryAddress> deliveryAddress) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accType = accType;
        this.passwordHash = passwordHash;
        this.deliveryAddress = deliveryAddress;
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

    public AccountType getAccType() {
        return accType;
    }

    public void setAccType(AccountType accType) {
        this.accType = accType;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public List<DeliveryAddress> getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(List<DeliveryAddress> deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(accType.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return userStatus == UserStatus.ACTIVE;
    }

    public UUID getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", accType=" + accType +
                ", passwordHash='" + passwordHash + '\'' +
                ", userStatus=" + userStatus +
                '}';
    }
}

