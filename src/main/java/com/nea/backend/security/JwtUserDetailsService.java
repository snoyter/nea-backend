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
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User userFromDB = userService.getUserByUsername(login);
        if (userFromDB == null) {
            throw new UsernameNotFoundException("Не найден пользователь с таким логином: " + login);
        }
        if (userFromDB.getLogin().equals(login)) {
            return new org.springframework.security.core.userdetails.User(
                    userFromDB.getLogin(),
                    userFromDB.getPassword(),
                    List.of(new SimpleGrantedAuthority(userFromDB.getUserType().getType()))
            );
        } else {
            throw new UsernameNotFoundException("Не найден пользователь с таким логином: " + login);
        }
    }
}
