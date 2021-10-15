package ru.geekbrains.traineeship.placeofgamesbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.EventDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.mapper.EventMapper;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.service.EventService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/events")
@Secured("ROLE_USER")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public List<EventDTO> findAll() {
        List<Event> events = eventService.findAll();
        return events.stream()
                .map(eventMapper::mapToEventDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EventDTO findById(@PathVariable Long id) {
        Event event = eventService.findById(id);
        return eventMapper.mapToEventDTO(event);
    }

    @PostMapping("/{id}/participants")
    public void addParticipant (@PathVariable Long id) {
        eventService.addParticipant(id);
    }

}
