package ru.geekbrains.traineeship.placeofgamesbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.InvalidPasswordException;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.UserAlreadyExistsException;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.UserNotFoundException;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Role;
import ru.geekbrains.traineeship.placeofgamesbackend.model.User;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.RoleRepository;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public User findByLoginWithRoles(String login) {
        return userRepository.findByLoginWithRoles(login);
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
    }

    public void createUser(String login, String password, String name) {
        userRepository.findByLogin(login).ifPresent(user -> {
            throw new UserAlreadyExistsException();
        });
        Role role = roleRepository.findById("ROLE_USER").orElseThrow(() -> new IllegalStateException("Role 'ROLE_USER' not found"));
        User user = User.builder()
                .login(login)
                .password(passwordEncoder.encode(password))
                .name(name)
                .roles(List.of(role))
                .build();
        userRepository.save(user);
    }


    public User findByLoginAndPassword(String login, String password) {
        User userEntity = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
        if (passwordEncoder.matches(password, userEntity.getPassword())) {
            return userEntity;
        } else {
            throw new InvalidPasswordException();
        }
    }
}