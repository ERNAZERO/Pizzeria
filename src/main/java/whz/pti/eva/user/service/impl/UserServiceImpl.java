package whz.pti.eva.user.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import whz.pti.eva.user.domain.dto.UserAddressUpdate;
import whz.pti.eva.user.repository.DeliveryAddressRepository;
import whz.pti.eva.user.repository.UserRepository;
import whz.pti.eva.user.domain.dto.UserRegistrationDto;
import whz.pti.eva.user.domain.entity.DeliveryAddress;
import whz.pti.eva.user.domain.entity.Users;
import whz.pti.eva.user.domain.enums.AccountType;
import whz.pti.eva.user.domain.enums.UserStatus;
import whz.pti.eva.user.service.UserService;

import java.util.List;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DeliveryAddressRepository deliveryAddressRepository;

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public void registerUser(UserRegistrationDto userRegistrationDto) {
        if(existsByEmail(userRegistrationDto.getEmail())) {
            throw new IllegalArgumentException("User with email " + userRegistrationDto.getEmail() + " already exists");
        }
        Users user = new Users();
        user.setEmail(userRegistrationDto.getEmail());
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setAccType(AccountType.valueOf(userRegistrationDto.getAccType()));
        user.setUserStatus(UserStatus.ACTIVE);
        user.setPasswordHash(passwordEncoder.encode(userRegistrationDto.getPasswordHash()));
        saveDeliveryAddress(user, userRegistrationDto.getStreet(), userRegistrationDto.getHouseNumber(), userRegistrationDto.getTown(), userRegistrationDto.getPostalCode());
        log.info("✅User with email {} has been registered", user.getEmail());
    }

    @Override
    public void blockUser(String userId) {
        Users users = userRepository.findById(UUID.fromString(userId)).orElse(null);
        if(users != null) {
            users.setUserStatus(UserStatus.BLOCKED);
            userRepository.save(users);
            log.info("User {} has been blocked", userRepository.getReferenceById(UUID.fromString(userId)));
        }
    }

    @Override
    public void restoreUser(String userId) {
        Users users = userRepository.findById(UUID.fromString(userId)).orElse(null);
        if(users != null) {
            users.setUserStatus(UserStatus.ACTIVE);
            userRepository.save(users);
            log.info("User {} has been restored", userRepository.getReferenceById(UUID.fromString(userId)));
        }
    }

    @Override
    public Users getUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId)).orElse(null);
    }

    @Override
    public void updateUser(UserRegistrationDto user, String id) {
        Users users = userRepository.findById(UUID.fromString(id)).orElse(null);
        if(users != null) {
            users.setFirstName(user.getFirstName());
            users.setLastName(user.getLastName());
            users.setEmail(user.getEmail());
            if(user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                users.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            }
            userRepository.save(users);
            log.info("User {}", userRepository.getReferenceById(UUID.fromString(id)));
        }
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<DeliveryAddress> getUserAddresses(String userId) {
        Users user = userRepository.findById(UUID.fromString(userId)).orElse(null);
        if(user != null) {
            return user.getDeliveryAddress();
        }
        return null;
    }

    @Override
    public DeliveryAddress getUserAddress(String userId, Long addressId) {
        Users user = userRepository.findById(UUID.fromString(userId)).orElse(null);
        if(user != null) {
            List<DeliveryAddress> deliveryAddresses = user.getDeliveryAddress();
            return deliveryAddresses.stream().filter(address -> address.getDeliveryAddressId().equals(addressId)).findFirst().orElse(null);
        }
        return null;
    }

    @Override
    public void updateAddress(UserAddressUpdate addressUpdate, String userId, Long addressId) {
        Users user = userRepository.findById(UUID.fromString(userId)).orElse(null);
        if(user != null) {
            List<DeliveryAddress> deliveryAddresses = user.getDeliveryAddress();
            DeliveryAddress deliveryAddress = deliveryAddresses.stream().filter(address -> address.getDeliveryAddressId().equals(addressId)).findFirst().orElse(null);
            if(deliveryAddress != null) {
                deliveryAddress.setStreet(addressUpdate.getStreet());
                deliveryAddress.setHouseNumber(addressUpdate.getHouseNumber());
                deliveryAddress.setTown(addressUpdate.getTown());
                deliveryAddress.setPostalCode(addressUpdate.getPostalCode());
                deliveryAddressRepository.save(deliveryAddress);
            }
        }
    }

    @Override
    public void addAddress(UserAddressUpdate addressUpdate, String userId) {
        userRepository.findById(UUID.fromString(userId)).ifPresent(user -> saveDeliveryAddress(user, addressUpdate.getStreet(), addressUpdate.getHouseNumber(), addressUpdate.getTown(), addressUpdate.getPostalCode()));
    }

    @Override
    public void deleteAddress(String userId, Long addressId) {
        log.info("Deleting address {}", addressId);
        Users user = userRepository.findById(UUID.fromString(userId)).orElse(null);
        if(user != null) {
            log.info("USER EXISTS");
            List<DeliveryAddress> deliveryAddresses = user.getDeliveryAddress();
            deliveryAddresses.removeIf(address -> address.getDeliveryAddressId().equals(addressId));
            user.setDeliveryAddress(deliveryAddresses);
            userRepository.save(user);
            deliveryAddressRepository.deleteById(addressId);
            log.info("Address {} has been deleted", addressId);
        }
    }

    private void saveDeliveryAddress(Users user, String street, String houseNumber, String town, String postalCode) {
        DeliveryAddress deliveryAddress = new DeliveryAddress();
        deliveryAddress.setStreet(street);
        deliveryAddress.setHouseNumber(houseNumber);
        deliveryAddress.setTown(town);
        deliveryAddress.setPostalCode(postalCode);
        deliveryAddressRepository.save(deliveryAddress);
        List<DeliveryAddress> deliveryAddresses = user.getDeliveryAddress();
        deliveryAddresses.add(deliveryAddress);
        user.setDeliveryAddress(deliveryAddresses);
        userRepository.save(user);
    }


}
