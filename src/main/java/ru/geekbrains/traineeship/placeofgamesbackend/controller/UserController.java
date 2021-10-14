package ru.geekbrains.traineeship.placeofgamesbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.RegistrationRequestDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    @PostMapping
    public void registerUser(@RequestBody RegistrationRequestDTO request) {
        userService.createUser(request.getLogin(), request.getPassword(), request.getName());
    }

    private final UserService userService;
}
