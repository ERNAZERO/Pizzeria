package whz.pti.eva.admin.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import whz.pti.eva.user.domain.dto.UserAddressUpdate;
import whz.pti.eva.user.domain.dto.UserRegistrationDto;
import whz.pti.eva.user.domain.entity.DeliveryAddress;
import whz.pti.eva.user.domain.enums.AccountType;
import whz.pti.eva.user.service.impl.UserServiceImpl;

import java.util.List;

@RequestMapping("/admin/users")
@Controller
public class AdminUsersController {


    @Autowired
    private UserServiceImpl userService;


    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admins/admin-users";
    }

    @GetMapping("/{id}")
    public String showUser(@PathVariable String id, Model model) {
        model.addAttribute("roles", AccountType.values());
        model.addAttribute("action", "/admin/users/"+id);
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("userEdit", new UserRegistrationDto());
        return "admins/admin-user-view";
    }

    @PostMapping("/{id}")
    public String updateUser(@PathVariable String id, Model model, UserRegistrationDto userRegistrationDto) {
        userService.updateUser(userRegistrationDto,id);
        return "redirect:/admin/users";
    }

    @GetMapping("/create-user")
    public String createUser(Model model){
        model.addAttribute("action", "/admin/users/create-user");
        model.addAttribute("user", null);
        model.addAttribute("userEdit", new UserRegistrationDto());
        return "admins/admin-user-view";
    }


    @PostMapping("/create-user")
    public String createUser(UserRegistrationDto userRegistrationDto){
        userService.registerUser(userRegistrationDto);
        return "redirect:/admin/users";
    }


    @GetMapping("/block/{id}")
    public String deleteUser(@PathVariable String id){
        userService.blockUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/restore/{id}")
    public String restoreUser(@PathVariable String id){
        userService.restoreUser(id);
        return "redirect:/admin/users";
    }


    @GetMapping("/address-edit/{id}")
    public String addressEdit(Model model, @PathVariable(required = true) String id) {
        model.addAttribute("userId", id);
        model.addAttribute("deliveryAddresses", userService.getUserAddresses(id));
        return "admins/admin-user-address-edit";
    }

    @GetMapping("/address-edit/{userId}/{id}")
    public String addressEdit(Model model, @PathVariable String userId, @PathVariable Long id) {
        List<DeliveryAddress> addressList = userService.getUserAddresses(userId);
        model.addAttribute("userId", userId);
        model.addAttribute("action", id);
        model.addAttribute("addressCount", addressList.size());
        model.addAttribute("deleteAction", "/admin/users/delete-address/"+ userId + "/" + id);
        model.addAttribute("updateAddress", new UserAddressUpdate());
        model.addAttribute("address", userService.getUserAddress(userId, id));
        return "admins/admin-user-address-edit-item";
    }

    @PostMapping("/address-edit/{userId}/{id}")
    public String addressEdit(UserAddressUpdate address, @PathVariable String userId, @PathVariable Long id) {
        userService.updateAddress(address, userId,id);
        return "redirect:/admin/users/address-edit/"+userId;
    }

    @GetMapping("/new-address/{userId}")
    public String newAddress(Model model, @PathVariable String userId) {
        model.addAttribute("userId", userId);
        model.addAttribute("action", "/admin/users/new-address/"+userId);
        model.addAttribute("address", new UserAddressUpdate());
        return "admins/admin-user-address-edit-item";
    }

    @PostMapping("/new-address/{userId}")
    public String newAddress(UserAddressUpdate address, @PathVariable String userId) {
        userService.addAddress(address, userId);
        return "redirect:/admin/users/address-edit/"+userId;
    }


    @GetMapping("/delete-address/{userId}/{id}")
    public String deleteAddress(@PathVariable String userId, @PathVariable Long id) {
        userService.deleteAddress(userId, id);
        return "redirect:/admin/users/address-edit/"+userId;
    }


}
