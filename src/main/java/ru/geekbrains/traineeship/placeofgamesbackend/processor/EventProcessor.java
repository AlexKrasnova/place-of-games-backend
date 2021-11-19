package ru.geekbrains.traineeship.placeofgamesbackend.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.event.EventDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.event.EventDetailsDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.event.EventToSaveDTO;
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

    public List<EventDTO> findAll(String currentUserLogin) {
        return eventService
                .findAll()
                .stream()
                .map(event -> eventMapper.mapToEventDTO(event, currentUserLogin))
                .collect(Collectors.toList());
    }

    public EventDetailsDTO findById(Long eventId, String currentUserLogin) {
        Event event = eventService.findById(eventId);
        return eventMapper.mapToEventDetailsDTO(event, currentUserLogin);
    }

    public void addParticipant(Long eventId, String currentUserLogin) {
        eventService.addParticipant(eventId, userService.findByLogin(currentUserLogin));
    }

    public void deleteParticipant(Long eventId, String currentUserLogin) {
        eventService.deleteParticipant(eventId, userService.findByLogin(currentUserLogin));
    }

    public void deleteEvent(Long eventId, String currentUserLogin) {
        eventService.deleteEvent(eventId, userService.findByLogin(currentUserLogin));
    }

    public Long create(EventToSaveDTO event, String currentUserLogin) {
        return eventService.create(eventMapper.mapToEvent(event), userService.findByLogin(currentUserLogin));
    }

    public List<EventDTO> findByOwner(String currentUserLogin) {
        return eventService
                .findByOwner(userService.findByLogin(currentUserLogin))
                .stream()
                .map(event -> eventMapper.mapToEventDTO(event, currentUserLogin))
                .collect(Collectors.toList());

    }

    public List<EventDTO> findByParticipant(String currentUserLogin) {
        return eventService
                .findByParticipant(userService.findByLogin(currentUserLogin))
                .stream()
                .map(event -> eventMapper.mapToEventDTO(event, currentUserLogin))
                .collect(Collectors.toList());

    }

    public void updateById(Long id, EventToSaveDTO event, String currentUserLogin) {
        eventService.updateById(id, eventMapper.mapToEvent(event), userService.findByLogin(currentUserLogin));
    }
}
