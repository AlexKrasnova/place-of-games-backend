package ru.geekbrains.traineeship.placeofgamesbackend.dto;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class AuthRequestDTO {

    @NotNull
    private String login;

    @NotNull
    private String password;
}
