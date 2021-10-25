package ru.geekbrains.traineeship.placeofgamesbackend.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.CurrentUserNotEnrolledException;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.EventIsFullException;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.EventNotFoundException;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.UserAlreadyEnrolledException;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.model.User;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.EventRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class EventServiceUnitTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    /**
     * Unit-тест на проверку успешного поиска мероприятия по id.
     * <p>
     * В тесте:
     * 1. Создается событие.
     * 2. Мочится метод поиска события по id у репозитория событий, чтобы при заданном id выдавалось созданное мероприятие.
     * 3. Проверяется, что результат вызова метода поиска по id у сервиса событий совпадает с созданным собитием.
     */
    @Test
    public void findByIdSuccess() {

        Long id = 1L;

        Event event = Event.builder()
                .id(id)
                .name("Футбол")
                .build();

        Optional<Event> result = Optional.of(event);
        Mockito.doReturn(result).when(eventRepository).findById(id);

        Event actual = eventService.findById(id);

        Assertions.assertThat(actual).isEqualTo(event);

    }

    /**
     * Unit-тест на проверку того, что при отсутствии мероприятия с заданным id,
     * в результате поиска мероприятия по id выбрасывается исключение EventNotFoundException.
     * <p>
     * В тесте:
     * 1. Создается пустой Optional<Event>.
     * 2. Мочится метод поиска события по id у репозитория событий, чтобы при заданном id выдавался пустой Optional<Event>.
     * 3. Проверяется, что, в результате вызова метода поиска по id у сервиса мероприятий, выбрасывается исключение EventNotFoundException.
     */
    @Test
    public void findByIdNotFound() {
        Long id = 1L;

        Optional<Event> result = Optional.empty();
        Mockito.doReturn(result).when(eventRepository).findById(id);

        assertThrows(EventNotFoundException.class, () -> eventService.findById(id));
    }

    /**
     * Unit-тест на проверку того, что при попытке записи на мероприятия пользователя, который уже записан,
     * выбрасывается исключение UserAlreadyEnrolledException.
     * <p>
     * В тесте:
     * 1. Создается пользователь.
     * 2. Создается мероприятие, в списке участников которого есть данный пользователь.
     * 3. Мочится метод поиска события по id у репозитория событий, чтобы при заданном id выдавалось созданное мероприятие.
     * 4. Проверяется, что, в результате вызова метода записи на мероприятие у сервиса мероприятий с заданным id мероприятия
     * и созданным пользователем, выбрасывается исключение UserAlreadyEnrolledException.
     */
    @Test
    public void addParticipantUserAlreadyEnrolled() {

        Long id = 1L;

        User user = User.builder()
                .login("user")
                .build();

        Event event = Event.builder()
                .id(id)
                .name("Футбол")
                .maxNumberOfParticipants(2)
                .participants(Collections.singleton(user))
                .build();

        Optional<Event> result = Optional.of(event);
        Mockito.doReturn(result).when(eventRepository).findById(id);

        assertThrows(UserAlreadyEnrolledException.class, () -> eventService.addParticipant(id, user));

    }

    /**
     * Unit-тест на проверку того, что при попытке записи на мероприятия пользователя, который уже записан,
     * выбрасывается исключение EventIsFullException.
     * <p>
     * В тесте:
     * 1. Создается 2 пользователя.
     * 2. Создается мероприятие, в списке участников которого есть один из заданных пользователей,
     * и максимальное количество участников которого равно 1.
     * 3. Мочится метод поиска события по id у репозитория событий, чтобы при заданном id выдавалось созданное мероприятие.
     * 4. Проверяется, что, в результате вызова метода записи на мероприятие у сервиса мероприятий с заданным id мероприятия
     * и вторым созданным пользователем, выбрасывается исключение EventIsFullException.
     */
    @Test
    public void addParticipantEventIsFull() {

        Long id = 1L;

        User user = User.builder()
                .login("user")
                .build();

        User user1 = User.builder()
                .login("user1")
                .build();

        Event event = Event.builder()
                .name("Футбол")
                .maxNumberOfParticipants(1)
                .participants(Collections.singleton(user))
                .build();

        Optional<Event> result = Optional.of(event);
        Mockito.doReturn(result).when(eventRepository).findById(id);

        assertThrows(EventIsFullException.class, () -> eventService.addParticipant(id, user1));

    }

    /**
     * Unit-тест проверку успешной записи на мероприятие.
     * <p>
     * В тесте:
     * 1. Создается пользователь.
     * 2. Создается мероприятие с пустым списком пользователей.
     * 3. Мочится метод поиска события по id у репозитория событий, чтобы при заданном id выдавалось созданное мероприятие.
     * 4. Мочится метод сохранения события у репозитория событий.
     * 5. Вызывается метод добавления участника в серивсе событий.
     * 6. Проверяется, что у замоченного репозитория событий вызывался метод сохранения мероприятия.
     */
    @Test
    public void addParticipantSuccess() {
        Long id = 1L;

        User user = User.builder()
                .login("user")
                .build();

        Event event = Event.builder()
                .name("Футбол")
                .maxNumberOfParticipants(2)
                .participants(new HashSet<>())
                .build();

        Optional<Event> result = Optional.of(event);
        Mockito.doReturn(result).when(eventRepository).findById(id);
        Mockito.doReturn(event).when(eventRepository).save(event);

        eventService.addParticipant(id, user);

        Mockito.verify(eventRepository).save(event);

    }

    /**
     * Unit-тест проверку успешной записи на мероприятие двух пользователей с одинаковыми именами,
     * но разными логинами.
     * <p>
     * В тесте:
     * 1. Создаются два пользователя с одинаковыми мероприятиями и разными логинами.
     * 2. Создается мероприятие с пустым списком пользователей.
     * 3. Мочится метод поиска события по id у репозитория событий, чтобы при заданном id выдавалось созданное мероприятие.
     * 4. Мочится метод сохранения события у репозитория событий.
     * 5. Поочередно вызывается метод добавления участника в серивсе событий с обоими созданными пользователями.
     * 6. Проверяется, что у замоченного репозитория событий дважды вызывался метод сохранения мероприятия.
     */
    @Test
    public void addParticipantsWithTheSameNamesSuccess() {

        Long id = 1L;

        User user = User.builder()
                .login("user")
                .name("user")
                .build();

        User user1 = User.builder()
                .login("user1")
                .name("user")
                .build();

        Event event = Event.builder()
                .name("Футбол")
                .maxNumberOfParticipants(2)
                .participants(new HashSet<>())
                .build();

        Optional<Event> result = Optional.of(event);
        Mockito.doReturn(result).when(eventRepository).findById(id);
        Mockito.doReturn(event).when(eventRepository).save(event);

        eventService.addParticipant(id, user);
        eventService.addParticipant(id, user1);

        Mockito.verify(eventRepository, Mockito.times(2)).save(event);

    }


    @Test
    public void deleteParticipantCurrentUserNotEnrolled() {

        Long id = 1L;

        User user = User.builder()
                .login("user")
                .build();

        Event event = Event.builder()
                .id(id)
                .name("Футбол")
                .maxNumberOfParticipants(2)
                .participants(Collections.emptySet())
                .build();

        Optional<Event> result = Optional.of(event);
        Mockito.doReturn(result).when(eventRepository).findById(id);

        assertThrows(CurrentUserNotEnrolledException.class, () -> eventService.deleteParticipant(id, user));

    }

    /**
     * Unit-тест проверку успешной записи на мероприятие.
     * <p>
     * В тесте:
     * 1. Создается пользователь.
     * 2. Создается мероприятие с записанным на него пользователем.
     * 3. Мочится метод поиска события по id у репозитория событий, чтобы при заданном id выдавалось созданное мероприятие.
     * 4. Мочится метод сохранения события у репозитория событий.
     * 5. Вызывается метод добавления участника в серивсе событий.
     * 6. Проверяется, что у замоченного репозитория событий вызывался метод сохранения мероприятия.
     */
    @Test
    public void deleteParticipantSuccess() {
        Long id = 1L;

        User user = User.builder()
                .login("user")
                .build();

        Set<User> users = new HashSet<>();
        users.add(user);

        Event event = Event.builder()
                .name("Футбол")
                .maxNumberOfParticipants(2)
                .participants(users)
                .build();

        Optional<Event> result = Optional.of(event);
        Mockito.doReturn(result).when(eventRepository).findById(id);
        Mockito.doReturn(event).when(eventRepository).save(event);

        eventService.deleteParticipant(id, user);

        Mockito.verify(eventRepository).save(event);

    }

}
