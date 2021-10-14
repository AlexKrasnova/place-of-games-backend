package ru.geekbrains.traineeship.placeofgamesbackend.dto;

import lombok.Data;

@Data
public class AuthRequestDTO {

    private String login;

    private String password;
}
