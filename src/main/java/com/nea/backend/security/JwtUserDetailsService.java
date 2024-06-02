package com.nea.backend.security;


import com.nea.backend.model.User;
import com.nea.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userFromDB = userService.getUserByUsername(username);
        if (userFromDB == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        if (userFromDB.getLogin().equals(username)) {
            return new org.springframework.security.core.userdetails.User(
                    userFromDB.getLogin(),
                    userFromDB.getPassword(),
                    List.of(new SimpleGrantedAuthority(userFromDB.getUserType().getType()))
            );
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
