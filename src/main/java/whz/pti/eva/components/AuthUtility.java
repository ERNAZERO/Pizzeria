package whz.pti.eva.components;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import whz.pti.eva.user.domain.entity.Users;

@Component
public class AuthUtility {

    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
    }

    public Users getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (isAuthenticated()) {
            return (Users) auth.getPrincipal();
        }
        return null;
    }

    public String getAuthenticatedUserId() {
        Users user = getAuthenticatedUser();
        return user != null ? user.getUserId().toString() : null;
    }
}

