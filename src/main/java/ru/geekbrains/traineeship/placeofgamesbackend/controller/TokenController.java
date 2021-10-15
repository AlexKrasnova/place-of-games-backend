package ru.geekbrains.traineeship.placeofgamesbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.AuthRequestDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.AuthResponseDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final AuthService authService;

    @PostMapping
    public AuthResponseDTO auth(@RequestBody @Valid AuthRequestDTO request) {
        return new AuthResponseDTO(authService.createToken(request.getLogin(), request.getPassword()));
    }
}
