package ru.geekbrains.traineeship.placeofgamesbackend.dto;

import lombok.Data;

@Data
public class RegistrationRequestDTO {

    String login;

    String password;

    String name;
}
