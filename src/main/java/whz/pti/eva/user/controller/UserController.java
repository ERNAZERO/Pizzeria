package whz.pti.eva.user.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import whz.pti.eva.components.AuthUtility;
import whz.pti.eva.components.UserUtility;
import whz.pti.eva.cart.service.CartService;
import whz.pti.eva.user.domain.dto.UserAddressUpdate;
import whz.pti.eva.user.domain.dto.UserRegistrationDto;
import whz.pti.eva.user.domain.entity.DeliveryAddress;
import whz.pti.eva.user.domain.entity.Users;
import whz.pti.eva.user.service.UserService;
import whz.pti.eva.user.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Objects;

@Controller
public class UserController {

    @Autowired
    private UserUtility userUtility;

    @Autowired
    private AuthUtility authUtility;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        userUtility.setupRegistrationModel(model, new UserRegistrationDto(), "");
        return "register";
    }

    @PostMapping("/register")
    public String register(UserRegistrationDto userRegistrationDto, Model model) {
        String registrationResult = userUtility.handleUserRegistration(userRegistrationDto, model);
        return Objects.requireNonNullElse(registrationResult, "redirect:/login"); // Redirect to registration if there's an error
    }

    @GetMapping("/customer/account")
    public String account(Model model) {
        userUtility.setupAccountModel(model);
        return "account";
    }

    @GetMapping("/customer/order-item/{id}")
    public String orderItem(Model model, @PathVariable int id) {
        model.addAttribute("order", cartService.getCartById(id));
        return "order-item";
    }

    @GetMapping("/customer/account-edit")
    public String accountEdit(Model model) {
        userUtility.setupAccountEditModel(model);
        return "account-edit";
    }

    @PostMapping("/customer/account-edit/{id}")
    public String accountEdit(UserRegistrationDto user, @PathVariable String id) {
        userService.updateUser(user, id);
        return "redirect:/account";
    }


    @GetMapping("/customer/address-edit")
    public String addressEdit(Model model) {
        model.addAttribute("deliveryAddresses", userService.getUserAddresses(authUtility.getAuthenticatedUserId()));
        return "address-edit";
    }

    @GetMapping("/customer/address-edit/{id}")
    public String addressEdit(Model model, @PathVariable Long id) {
        List<DeliveryAddress> addressList = userService.getUserAddresses(authUtility.getAuthenticatedUserId());
        model.addAttribute("action", "/customer/address-edit/" + id);
        model.addAttribute("deleteAction", "/customer/delete-address/" + id);
        model.addAttribute("updateAddress", new UserAddressUpdate());
        model.addAttribute("addressCount", addressList.size());
        model.addAttribute("address", userService.getUserAddress(authUtility.getAuthenticatedUserId(), id));
        return "address-edit-item";
    }

    @PostMapping("/customer/address-edit/{id}")
    public String addressEdit(UserAddressUpdate address, @PathVariable Long id) {
        userService.updateAddress(address,authUtility.getAuthenticatedUserId(),id);
        return "redirect:/customer/address-edit";
    }

    @GetMapping("customer/new-address")
    public String newAddress(Model model) {
        model.addAttribute("action", "/customer/new-address");
        model.addAttribute("address", new UserAddressUpdate());
        return "address-edit-item";
    }

    @PostMapping("customer/new-address")
    public String newAddress(UserAddressUpdate address) {
        userService.addAddress(address, authUtility.getAuthenticatedUserId());
        return "redirect:/customer/address-edit";
    }


    @GetMapping("/customer/delete-address/{id}")
    public String deleteAddress(@PathVariable Long id) {
        userService.deleteAddress(authUtility.getAuthenticatedUserId(), id);
        return "redirect:/customer/address-edit";
    }

    @GetMapping("/customer/orderMaking")
    public String getOrderMakingPage() {
        return "orderMaking";
    }


}


