package whz.pti.eva.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import whz.pti.eva.user.domain.dto.UserRegistrationDto;
import whz.pti.eva.user.service.impl.UserServiceImpl;


@Component
public class UserInitializer implements CommandLineRunner {

    private final UserServiceImpl userService;
    private static final Logger log = LoggerFactory.getLogger(UserInitializer.class);


    public UserInitializer(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        UserRegistrationDto bnutz = new UserRegistrationDto();
        bnutz.setEmail("bnutz");
        bnutz.setFirstName("Benedikt");
        bnutz.setLastName("Nutz");
        bnutz.setPasswordHash("n1");
        bnutz.setStreet("Hauptstrasse");
        bnutz.setHouseNumber("1");
        bnutz.setTown("Würzburg");
        bnutz.setPostalCode("97070");
        bnutz.setAccType("CUSTOMER");
        userService.registerUser(bnutz);

        UserRegistrationDto cnutz = new UserRegistrationDto();
        cnutz.setEmail("cnutz");
        cnutz.setFirstName("Christoph");
        cnutz.setLastName("Nutz");
        cnutz.setPasswordHash("n2");
        cnutz.setStreet("Hauptstrasse");
        cnutz.setHouseNumber("2");
        cnutz.setTown("Würzburg");
        cnutz.setPostalCode("97070");
        cnutz.setAccType("CUSTOMER");
        userService.registerUser(cnutz);

        UserRegistrationDto admin = new UserRegistrationDto();
        admin.setEmail("admin");
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setPasswordHash("a1");
        admin.setStreet("Hauptstrasse");
        admin.setHouseNumber("3");
        admin.setTown("Würzburg");
        admin.setPostalCode("97070");
        admin.setAccType("ADMIN");
        userService.registerUser(admin);

        log.info("✅Users have been initialized");
    }
}
