package ru.geekbrains.traineeship.placeofgamesbackend.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.EventIsFullException;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.EventNotFoundException;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.UserAlreadyEnrolledException;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Place;
import ru.geekbrains.traineeship.placeofgamesbackend.model.User;
import ru.geekbrains.traineeship.placeofgamesbackend.model.WorkingHours;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.geekbrains.traineeship.placeofgamesbackend.model.Category.BASKETBALL;
import static ru.geekbrains.traineeship.placeofgamesbackend.model.DayOfWeek.FRIDAY;

@ExtendWith(MockitoExtension.class)
public class EventServiceUnitTest {

    @Mock
    private EventRepository eventRepository;

    @Autowired
    @InjectMocks
    private EventService eventService;

    @Test
    public void findByIdSuccess() {
        Long id = 1L;

        Event event = Event.builder()
                .name("Футбол")
                .build();

        Optional<Event> result = Optional.of(event);
        Mockito.doReturn(result).when(eventRepository).findById(id);

        Event actual = eventService.findById(id);

        Assertions.assertThat(actual).isEqualTo(event);

    }

    @Test
    public void findByIdNotFound() {
        Long id = 1L;

        Optional<Event> result = Optional.empty();
        Mockito.doReturn(result).when(eventRepository).findById(id);

        assertThrows(EventNotFoundException.class, () -> eventService.findById(id));
    }

    @Test
    public void addParticipantUserAlreadyEnrolled() {

        Long id = 1L;

        User user = User.builder()
                .name("Вася")
                .build();

        Event event = Event.builder()
                .name("Футбол")
                .maxNumberOfParticipants(2)
                .participants(Collections.singleton(user))
                .build();

        Optional<Event> result = Optional.of(event);
        Mockito.doReturn(result).when(eventRepository).findById(id);

        assertThrows(UserAlreadyEnrolledException.class, () -> eventService.addParticipant(id, user));

    }

    @Test
    public void addParticipantEventIsFull() {

        Long id = 1L;

        User user = User.builder()
                .name("Вася")
                .build();

        Event event = Event.builder()
                .name("Футбол")
                .maxNumberOfParticipants(1)
                .participants(Collections.singleton(user))
                .build();

        Optional<Event> result = Optional.of(event);
        Mockito.doReturn(result).when(eventRepository).findById(id);

        assertThrows(EventIsFullException.class, () -> eventService.addParticipant(id, user));

    }

}
