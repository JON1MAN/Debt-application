package com.debtapp.config.security;

import com.debtapp.user.controller.exceptions.UserNotFoundException;
import com.debtapp.user.dao.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class SecurityUtils {

    public static Integer getCurrentAuthenticatedUserId() {
        return getCurrentUserId().orElseThrow(
                () -> new UserNotFoundException("No auth user found")
        );
    }

    private static Optional<Integer> getCurrentUserId() {
        return getCurrentOptionalUser()
                .map(User::getId);
    }

    public static User getCurrentUser() {
        return getCurrentOptionalUser().orElseThrow(
                () -> new UserNotFoundException(("No auth user found"))
        );
    }

    private static Optional<User> getCurrentOptionalUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        } else if (authentication.getPrincipal() instanceof SecurityUser securityUser) {
            return Optional.ofNullable(securityUser.getUser());
        }
        return Optional.empty();
    }

}
