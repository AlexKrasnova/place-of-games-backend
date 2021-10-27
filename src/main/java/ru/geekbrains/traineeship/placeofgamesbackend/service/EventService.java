package ru.geekbrains.traineeship.placeofgamesbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.CurrentUserNotEnrolledException;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.EventIsFullException;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.EventNotFoundException;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.UserAlreadyEnrolledException;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.model.User;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.EventRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<Event> findAll() {
        return eventRepository.findAllWithPlacesAndUsers();
    }

    public Event findById(Long id) {
        return eventRepository.findById(id).orElseThrow(EventNotFoundException::new);
    }

    @Transactional
    public void addParticipant(Long eventId, User user) {
        Event event = findById(eventId);
        Set<User> users = event.getParticipants();
        if (users.size() >= event.getMaxNumberOfParticipants()) {
            throw new EventIsFullException();
        }
        if (users.stream().anyMatch(participant -> participant.getLogin().equals(user.getLogin()))) {
            throw new UserAlreadyEnrolledException();
        }
        users.add(user);
        eventRepository.save(event);
    }

    @Transactional
    public void deleteParticipant(Long eventId, User user) {
        Event event = findById(eventId);
        Set<User> users = event.getParticipants();
        User participantToDelete = null;
        for (User participant : users) {
            if (participant.getLogin().equals(user.getLogin()))
                participantToDelete = participant;
        }
        if (participantToDelete == null) {
            throw new CurrentUserNotEnrolledException();
        }
        users.remove(participantToDelete);
        eventRepository.save(event);
    }
}
