package ru.geekbrains.traineeship.placeofgamesbackend.dto;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class RegistrationRequestDTO {

    @NotNull
    String login;

    @NotNull
    String password;

    String name;

}
