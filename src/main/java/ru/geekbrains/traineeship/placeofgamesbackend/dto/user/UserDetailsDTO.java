package ru.geekbrains.traineeship.placeofgamesbackend.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailsDTO {

    private Long id;

    private String name;

    private String login;

}
