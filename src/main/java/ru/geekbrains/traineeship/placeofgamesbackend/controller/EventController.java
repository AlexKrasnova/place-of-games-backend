package ru.geekbrains.traineeship.placeofgamesbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.event.EventDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.event.EventDetailsDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.event.EventToCreateDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.processor.EventProcessor;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@Secured("ROLE_USER")
@RequiredArgsConstructor
public class EventController {

    private final EventProcessor eventProcessor;

    @GetMapping
    public List<EventDTO> findAll(Principal principal) {
        return eventProcessor.findAll(principal.getName());
    }

    @GetMapping("/{id}")
    public EventDetailsDTO findById(@PathVariable Long id, Principal principal) {
        return eventProcessor.findById(id, principal.getName());
    }

    @PostMapping("/{id}/participants")
    public void addParticipant(@PathVariable Long id, Principal principal) {
        eventProcessor.addParticipant(id, principal.getName());
    }

    @DeleteMapping("/{id}/participants")
    public void deleteParticipant(@PathVariable Long id, Principal principal) {
        eventProcessor.deleteParticipant(id, principal.getName());
    }

    @SneakyThrows
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody EventToCreateDTO event, Principal principal) {
        Long newId = eventProcessor.create(event, principal.getName());
        return ResponseEntity.created(new URI("/api/v1/events/" + newId)).body(newId);
    }

}
