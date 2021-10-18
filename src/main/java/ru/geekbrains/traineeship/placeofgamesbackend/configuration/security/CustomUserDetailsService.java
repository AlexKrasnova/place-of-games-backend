package ru.geekbrains.traineeship.placeofgamesbackend.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.geekbrains.traineeship.placeofgamesbackend.model.User;
import ru.geekbrains.traineeship.placeofgamesbackend.service.UserService;

@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByLoginWithRoles(username);
        return CustomUserDetails.fromUserEntityToCustomUserDetails(user);
    }
}
