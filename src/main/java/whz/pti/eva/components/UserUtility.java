package whz.pti.eva.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import whz.pti.eva.user.domain.dto.UserRegistrationDto;
import whz.pti.eva.user.domain.entity.Users;
import whz.pti.eva.user.domain.enums.AccountType;
import whz.pti.eva.cart.service.CartService;
import whz.pti.eva.user.service.UserService;

@Component
public class UserUtility {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private AuthUtility authUtility;

    public void setupRegistrationModel(Model model, UserRegistrationDto userRegistrationDto, String error) {
        model.addAttribute("user", userRegistrationDto);
        model.addAttribute("error", error);
    }

    public String handleUserRegistration(UserRegistrationDto userRegistrationDto, Model model) {
        if (!userRegistrationDto.getPasswordHash().equals(userRegistrationDto.getPasswordHashConfirm())) {
            setupRegistrationModel(model, userRegistrationDto, "Passwörter stimmen nicht überein");
            return "register";
        }

        if (userService.existsByEmail(userRegistrationDto.getEmail())) {
            setupRegistrationModel(model, userRegistrationDto, "User mit dieser Email existiert bereits");
            return "register";
        }

        userService.registerUser(userRegistrationDto);
        return null; // Success
    }

    public void setupAccountModel(Model model) {
        Users user = authUtility.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("adminRole", AccountType.ADMIN);
            model.addAttribute("user", userService.getUserById(String.valueOf(user.getUserId())));
            model.addAttribute("orders", cartService.allOrdersByUser(user));
        }
    }

    public void setupAccountEditModel(Model model) {
        Users user = authUtility.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", userService.getUserById(String.valueOf(user.getUserId())));
            model.addAttribute("userEdit", new UserRegistrationDto());
        }
    }
}

