package ru.geekbrains.traineeship.placeofgamesbackend.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.EventDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.mapper.EventMapper;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.service.EventService;
import ru.geekbrains.traineeship.placeofgamesbackend.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class EventProcessor {

    private final EventService eventService;

    private final UserService userService;

    private final EventMapper eventMapper;

    public List<EventDTO> findAll(String currentUser) {
        return eventService
                .findAll()
                .stream()
                .map(event -> eventMapper.mapToEventDTO(event, currentUser))
                .collect(Collectors.toList());
    }

    public EventDTO findById(Long eventId, String currentUser) {
        Event event = eventService.findById(eventId);
        return eventMapper.mapToEventDTO(event, currentUser);
    }

    public void addParticipant(Long eventId, String currentUser) {
        eventService.addParticipant(eventId, userService.findByLogin(currentUser));
    }

    public void deleteParticipant(Long eventId, String currentUser) {
        eventService.deleteParticipant(eventId, userService.findByLogin(currentUser));
    }
}
