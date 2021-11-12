package ru.geekbrains.traineeship.placeofgamesbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.RegistrationRequestDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.service.UserService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @SneakyThrows
    @PostMapping
    public ResponseEntity<Void> registerUser(@RequestBody @Valid RegistrationRequestDTO request) {
        userService.createUser(request.getLogin(), request.getPassword(), request.getName());
        return ResponseEntity.created(new URI("/api/v1/users/" + request.getLogin())).body(null);
    }
}
