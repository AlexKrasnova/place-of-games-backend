package ru.geekbrains.traineeship.placeofgamesbackend.mapper;

import org.springframework.stereotype.Component;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.UserDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.User;

@Component
public class UserMapper {

    public UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
