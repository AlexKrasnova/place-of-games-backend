package ru.geekbrains.traineeship.placeofgamesbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.traineeship.placeofgamesbackend.model.User;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;

    private final JwtProvider jwtProvider;

    public String createToken(String login, String password) {
        User user = userService.findByLoginAndPassword(login, password);
        return jwtProvider.generateToken(user.getLogin());
    }

}
