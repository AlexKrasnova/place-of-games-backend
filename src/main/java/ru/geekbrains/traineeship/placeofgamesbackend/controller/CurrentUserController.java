package ru.geekbrains.traineeship.placeofgamesbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.event.EventDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.user.UserDetailsDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.mapper.UserMapper;
import ru.geekbrains.traineeship.placeofgamesbackend.processor.EventProcessor;
import ru.geekbrains.traineeship.placeofgamesbackend.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Secured("ROLE_USER")
@RequiredArgsConstructor
public class CurrentUserController {

    private final UserMapper userMapper;

    private final UserService userService;

    private final EventProcessor eventProcessor;

    @GetMapping
    public UserDetailsDTO getUserDetails(Principal principal) {
        return userMapper.mapToUserDetailsDTO(userService.findByLogin(principal.getName()));
    }

    @GetMapping("/owned-events")
    public List<EventDTO> getOwnedEvents(Principal principal) {

        return eventProcessor.findByOwner(principal.getName());

    }

    @GetMapping("/events-to-participate")
    public List<EventDTO> getEventsToParticipate(Principal principal) {

        return eventProcessor.findByParticipant(principal.getName());

    }

}
