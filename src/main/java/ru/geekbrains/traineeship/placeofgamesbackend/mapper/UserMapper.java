package ru.geekbrains.traineeship.placeofgamesbackend.mapper;

import org.springframework.stereotype.Component;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.user.UserDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.user.UserDetailsDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.User;

@Component
public class UserMapper {

    public UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public UserDetailsDTO mapToUserDetailsDTO(User user) {
        return UserDetailsDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .login(user.getLogin())
                .build();
    }
}
