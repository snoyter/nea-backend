package com.nea.backend.security;

import com.nea.backend.exception.ApiError;
import com.nea.backend.model.User;
import com.nea.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;



@Slf4j
@Component
@RequestScope
@RequiredArgsConstructor
public class CurrentUser {
    private final UserService userService;
    private User user;

    public UserDetails getJwtUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof String) {
            return null;
        }
        return (UserDetails) principal;
    }

    public User getUser() {
        if (user == null) {
            UserDetails jwtUser = getJwtUser();
            if (jwtUser == null) {
                throw new ApiError.UserNotLoggedIn();
            }
            user = userService.getUserByUsername(jwtUser.getUsername());
        }
        return user;
    }

}

