package whz.pti.eva.user.service;

import whz.pti.eva.user.domain.dto.UserAddressUpdate;
import whz.pti.eva.user.domain.dto.UserRegistrationDto;
import whz.pti.eva.user.domain.entity.DeliveryAddress;
import whz.pti.eva.user.domain.entity.Users;

import java.util.List;

public interface UserService {

    boolean existsByEmail(String email);

    void registerUser(UserRegistrationDto userRegistrationDto);

    void blockUser(String userId);

    void restoreUser(String userId);

    Users getUserById(String userId);

    void updateUser(UserRegistrationDto user, String id);

    List<Users> getAllUsers();

    List<DeliveryAddress> getUserAddresses(String userId);

    DeliveryAddress getUserAddress(String userId, Long addressId);

    void updateAddress(UserAddressUpdate addressUpdate, String userId, Long addressId);

    void addAddress(UserAddressUpdate addressUpdate, String userId);

    void deleteAddress(String userId, Long addressId);

}
