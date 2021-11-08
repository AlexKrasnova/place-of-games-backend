package ru.geekbrains.traineeship.placeofgamesbackend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.geekbrains.traineeship.placeofgamesbackend.AbstractIntegrationTest;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Place;
import ru.geekbrains.traineeship.placeofgamesbackend.model.User;
import ru.geekbrains.traineeship.placeofgamesbackend.model.WorkingHours;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.EventRepository;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.PlaceRepository;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.UserRepository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.geekbrains.traineeship.placeofgamesbackend.model.Category.FOOTBALL;

public class CurrentUserControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String BASE_URL = "/api/v1/user";

    private static final String TEST_USER = "testUser";

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        eventRepository.deleteAll();
        userRepository.deleteAll();
        placeRepository.deleteAll();
    }

    /**
     * Интеграционный тест на проверку успешного получения информации о текущем пользователе.
     * <p>
     * В тесте:
     * 1. Создается и добавляется в бд пользователь.
     * 2. Вызывается GET /api/v1/user.
     * 3. Проверяется, что в результате запроса получается статус 200 OK.
     * 4. Проверяется, что полученные данные пользователя соответсвтуют данным созданного нами пользователя.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void getUserDetailsSuccess() throws Exception {

        User user = createUser(TEST_USER);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        User actual = getResponse(result, User.class);

        Assertions.assertThat(actual.getId()).isEqualTo(user.getId());
        Assertions.assertThat(actual.getLogin()).isEqualTo(user.getLogin());
        Assertions.assertThat(actual.getName()).isEqualTo(user.getName());

    }

    /**
     * Интеграционный тест на проверку успешного получения списка мероприятий, которые создал и администрирует пользователь,
     * когда пользователь не администрирует ни одного мероприятия.
     * <p>
     * В тесте:
     * 1. Создается и добавляется в бд пользователь.
     * 2. Вызывается GET /api/v1/user/owned-events.
     * 3. Проверяется, что в результате запроса получается статус 200 OK.
     * 4. Проверяется, что результат является пустым списком.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void getOwnedEventsUserDoesNotOwnEventsSuccess() throws Exception {

        User user = createUser(TEST_USER);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/owned-events"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        List<Event> actual = getResponse(result, new TypeReference<>() {
        });

        Assertions.assertThat(actual).isEmpty();
    }

    /**
     * Интеграционный тест на проверку успешного получения списка мероприятий, на которые записан пользователь,
     * когда пользователь не записан ни на одно мероприятие.
     * <p>
     * В тесте:
     * 1. Создается и добавляется в бд пользователь.
     * 2. Вызывается GET /api/v1/user/events-to-participate.
     * 3. Проверяется, что в результате запроса получается статус 200 OK.
     * 4. Проверяется, что результат является пустым списком.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void getEventsToParticipateUserParticipatesInNoEventsSuccess() throws Exception {

        User user = createUser(TEST_USER);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/events-to-participate"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        List<Event> actual = getResponse(result, new TypeReference<>() {
        });

        Assertions.assertThat(actual).isEmpty();
    }

    /**
     * Интеграционный тест на проверку успешного получения списка мероприятий, на которые записан пользователь,
     * когда пользователь записан на одно мероприятие.
     * <p>
     * В тесте:
     * 1. Создается и добавляется в бд два разных пользователя.
     * 2. Создается и добавляется в бд два мероприятия с разными owner-ами.
     * 3. Вызывается GET /api/v1/user/owned-events.
     * 4. Проверяется, что в результате запроса получается статус 200 OK.
     * 5. Проверяется, что результат является список с одним элементом. Сверяется, что мероприятие в списке сооветствует
     * созданному нами мероприятию, участником которого является текущий пользователь.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void getOwnedEventsUserOwnsOneEventSuccess() throws Exception {

        User user = createUser(TEST_USER);
        User user1 = createUser(TEST_USER + 1);

        Event event = createEvent("Футбол", user);
        createEvent("Футбол 1", user1);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/owned-events"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        List<Event> actual = getResponse(result, new TypeReference<>() {
        });

        Assertions.assertThat(actual.size()).isEqualTo(1);
        Assertions.assertThat(actual.get(0).getId()).isEqualTo(event.getId());
        Assertions.assertThat(actual.get(0).getName()).isEqualTo(event.getName());


    }

    /**
     * Интеграционный тест на проверку успешного получения списка мероприятий, на которые записан пользователь,
     * когда пользователь записан на одно мероприятие.
     * <p>
     * В тесте:
     * В тесте:
     * 1. Создается и добавляется в бд пользователь.
     * 2. Создается и добавляется в бд два мероприятия, в список участников одного из них добавляется текущий пользователь.
     * 3. Вызывается GET /api/v1/user/events-to-participate.
     * 4. Проверяется, что в результате запроса получается статус 200 OK.
     * 5. Проверяется, что результат является список с одним элементом. Сверяется, что мероприятие в списке сооветствует
     * созданному нами мероприятию, owner-ом которого является текущий пользователь.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void getOwnedEventsUserHasOneEventToParticipateSuccess() throws Exception {

        User user = createUser(TEST_USER);

        Event event = createEvent("Футбол", user);
        event.getParticipants().add(user);
        eventRepository.save(event);

        createEvent("Футбол 1", user);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/events-to-participate"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        List<Event> actual = getResponse(result, new TypeReference<>() {
        });

        Assertions.assertThat(actual.size()).isEqualTo(1);
        Assertions.assertThat(actual.get(0).getId()).isEqualTo(event.getId());
        Assertions.assertThat(actual.get(0).getName()).isEqualTo(event.getName());

    }

    private User createUser(String login) {

        User user = User.builder()
                .login(login)
                .name("name")
                .password("1")
                .build();

        return userRepository.save(user);

    }

    private Event createEvent(String name, User owner) {

        Place place = createPlaceWithWorkingHours();

        Set<User> participants = new HashSet<>();

        Event event = Event.builder()
                .name(name)
                .time(LocalDateTime.of(2021, 11, 12, 12, 0))
                .duration(120)
                .placeId(place.getId())
                .place(place)
                .maxNumberOfParticipants(12)
                .description("Какое-то мероприятие")
                .category(FOOTBALL)
                .participants(participants)
                .ownerId(owner.getId())
                .build();
        event = eventRepository.save(event);

        return event;

    }

    private Place createPlaceWithWorkingHours() {
        List<WorkingHours> workingHoursList = new ArrayList<>();
        workingHoursList
                .add(WorkingHours.builder()
                        .dayOfWeek(DayOfWeek.FRIDAY)
                        .startTime(LocalTime.of(10, 0))
                        .endTime(LocalTime.of(16, 0))
                        .build());

        Place place = Place.builder()
                .name("Стадион")
                .address("ул. Ленина, д. 1")
                .workingHoursList(workingHoursList)
                .build();

        for (WorkingHours workingHours : workingHoursList)
            workingHours.setPlace(place);

        return placeRepository.save(place);
    }
}
