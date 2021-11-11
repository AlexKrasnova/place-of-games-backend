package ru.geekbrains.traineeship.placeofgamesbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.*;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Place;
import ru.geekbrains.traineeship.placeofgamesbackend.model.TimePeriod;
import ru.geekbrains.traineeship.placeofgamesbackend.model.User;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.EventRepository;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.PlaceRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final PlaceRepository placeRepository;

    private final PlaceService placeService;

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

    @Transactional
    public void deleteEvent(Long eventId, User user){
        Event event = findById(eventId);
        if(event.getOwner().equals(user)) {
            eventRepository.deleteById(eventId);
        } else { throw new CurrentUserNotEventOwnerException(); }
    }

    @Transactional
    public Long create(Event event, User user) {
        event.setOwnerId(user.getId());

        Place place = placeService.findById(event.getPlaceId());
        TimePeriod eventTime = TimePeriod.builder()
                .startTime(event.getTime())
                .endTime(event.getTime().plusMinutes(event.getDuration()))
                .build();
        if (!placeService.isTimeFree(event.getPlaceId(), eventTime)) {
            throw new NotWorkingOrNotFreeTimePeriodException();
        }

        place.getEvents().add(event);
        placeRepository.save(place);

        return eventRepository.save(event).getId();
    }

    public List<Event> findByOwner(User user) {
        return eventRepository.findByOwner(user.getId());
    }

    public List<Event> findByParticipant(User user) {
        return eventRepository.findByParticipantId(user.getId());
    }
}
