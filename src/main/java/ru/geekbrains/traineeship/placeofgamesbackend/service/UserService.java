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
        return userRepository.findById(login).orElseThrow(UserNotFoundException::new);
    }

    public void createUser(String login, String password, String name) {
        userRepository.findById(login).ifPresent(user -> {
            throw new UserAlreadyExistsException();
        });
        Role role = roleRepository.findById("ROLE_USER").orElseThrow(() -> new IllegalStateException("Role 'ROLE_USER' not found"));
        User user = new User(login, passwordEncoder.encode(password), name, List.of(role));
        userRepository.save(user);
    }


    public User findByLoginAndPassword(String login, String password) {
        User userEntity = userRepository.findById(login).orElseThrow(UserNotFoundException::new);
        if (passwordEncoder.matches(password, userEntity.getPassword())) {
            return userEntity;
        } else {
            throw new InvalidPasswordException();
        }
    }
}