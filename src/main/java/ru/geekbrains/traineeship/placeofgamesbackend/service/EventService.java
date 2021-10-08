package ru.geekbrains.traineeship.placeofgamesbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.EventIsFullException;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.EventNotFoundException;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<Event> findAll() {
        return eventRepository.findAllWithPlaces();
    }

    public Event findById(Long id) {
        return eventRepository.findById(id).orElseThrow(EventNotFoundException::new);
    }

    @Transactional
    public void addParticipant(Long eventId) {
        findById(eventId);
        if (eventRepository.addParticipant(eventId) == 0) {
            throw new EventIsFullException();
        }
    }
}
