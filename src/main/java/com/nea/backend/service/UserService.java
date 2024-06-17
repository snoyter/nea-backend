package com.nea.backend.service;

import com.nea.backend.dto.UserChangePasswordDto;
import com.nea.backend.dto.UserChangeTypeDto;
import com.nea.backend.dto.UserCreateDTO;
import com.nea.backend.emuns.Role;
import com.nea.backend.exception.ApiError;
import com.nea.backend.model.User;
import com.nea.backend.model.UserType;
import com.nea.backend.repository.UserRepository;
import com.nea.backend.repository.UserTypeRepository;
import com.nea.backend.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;

    private final CurrentUser currentUser;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getOneById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Нет такой сущности"));
    }

    public User create(UserCreateDTO dto) {
        UserType type = userTypeRepository.findById(Role.USER.getId())
                .orElseThrow(() -> new RuntimeException("Нет такого типа пользователей"));
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(new User(dto, type));
    }

    public void createNewEmployee(UserCreateDTO dto) {
        UserType type = userTypeRepository.findById(Role.ADMIN.getId())
                .orElseThrow(() -> new RuntimeException("Нет такого типа пользователей"));
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(new User(dto, type));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByLogin(username)
                .orElseThrow(ApiError.UserNotExist::new);
    }

    public void changeUserType(UserChangeTypeDto userChangeTypeDto) {
        User user = getOneById(userChangeTypeDto.getId());
        UserType type = userTypeRepository.findById(userChangeTypeDto.getUserTypeId())
                .orElseThrow(() -> new RuntimeException("Нет такого типа пользователей"));
        user.setUserType(type);
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    public void changePassword(
            UserChangePasswordDto dto
    ) {
        User user = userRepository.findById(currentUser.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Нет такого пользователя!"));
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Пароли не одинаковые!");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
    }

    public User giveAdmin(Integer id) {
        changeUserType(new UserChangeTypeDto(
                id,
                1
        ));
        return getOneById(id);
    }
}
