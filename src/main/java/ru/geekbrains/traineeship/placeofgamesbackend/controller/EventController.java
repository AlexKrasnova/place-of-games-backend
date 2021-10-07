package ru.geekbrains.traineeship.placeofgamesbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.EventWithPlaceDetailsDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.mapper.EventMapper;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.service.EventService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public List<EventWithPlaceDetailsDTO> findAll() {
        List<Event> events = eventService.findAll();
        return events.stream()
                .map(eventMapper::convertEventToEventWithPlaceDetailsDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EventWithPlaceDetailsDTO findById(@PathVariable Long id) {
        Event event = eventService.findById(id);
        return eventMapper.convertEventToEventWithPlaceDetailsDTO(event);
    }

    @PostMapping("/{id}/participants")
    public void addParticipant (@PathVariable Long id) {
        eventService.addParticipant(id);
    }

}
