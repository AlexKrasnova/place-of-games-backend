package ru.geekbrains.traineeship.placeofgamesbackend.controller;

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
import ru.geekbrains.traineeship.placeofgamesbackend.dto.ErrorDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.EventDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.PlaceDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.WorkingHoursDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Place;
import ru.geekbrains.traineeship.placeofgamesbackend.model.User;
import ru.geekbrains.traineeship.placeofgamesbackend.model.WorkingHours;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.EventRepository;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.PlaceRepository;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.geekbrains.traineeship.placeofgamesbackend.dto.ErrorType.*;
import static ru.geekbrains.traineeship.placeofgamesbackend.model.Category.BASKETBALL;
import static ru.geekbrains.traineeship.placeofgamesbackend.model.DayOfWeek.FRIDAY;


public class EventControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String BASE_URL = "/api/v1/events";

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
     * Интеграционный тест на проверку успешного поиска списка мероприятий.
     * <p>
     * В тесте:
     * 1. Создается список мероприятий и добавляется в базу данных с помощью EventRepository.
     * 2. Вызывается GET /api/v1/events.
     * 3. Проверяется, что в результате запроса получается статус 200 OK и сверяется результат.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void findAllSuccess() throws Exception {

        User user = User.builder()
                .login(TEST_USER)
                .name("Вася")
                .build();
        userRepository.save(user);

        Event event = createEvent();

        event.getParticipants().add(user);
        eventRepository.save(event);

        List<EventDTO> eventDTOList = new ArrayList<>();
        eventDTOList.add(EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .time(event.getTime())
                .duration(event.getDuration())
                .place(PlaceDTO.builder()
                        .id(event.getPlaceId())
                        .name(event.getPlace().getName())
                        .address(event.getPlace().getAddress())
                        .build())
                .maxNumberOfParticipants(event.getMaxNumberOfParticipants())
                .category(event.getCategory())
                .numberOfParticipants(event.getParticipants().size())
                .isCurrentUserEnrolled(true)
                .build());

        mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andExpect(MockMvcResultMatchers
                        .content()
                        .json(objectMapper.writeValueAsString(eventDTOList), true));

    }

    /**
     * Интеграционный тест для проверки отрицательного сценария поиска списка мероприятий, когда пользователь не аутентифицирован.
     * <p>
     * В тесте:
     * 1. Создается мероприятие
     * 2. Вызывается GET /api/v1/events.
     * 3. Проверяется, что в результате запроса получается статус 401 Unauthorized.
     * 4. Сверяется, что в ответе отправляется соответствующая ошибка.
     */
    @Test
    public void findAllUnauthorized() throws Exception {

        createEvent();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isUnauthorized())
                .andReturn();

        ErrorDTO errorDTO = getResponse(result, ErrorDTO.class);

        Assertions.assertThat(errorDTO.getErrorType()).isEqualTo(USER_UNAUTHORIZED);
        Assertions.assertThat(errorDTO.getMessage()).isEqualTo(USER_UNAUTHORIZED.getDescription());

    }

    /**
     * Интеграционный тест для проверки отрицательного сценария поиска мероприятия по id, при котором мероприятие с заданным id не найдено.
     * <p>
     * В тесте:
     * 1. Вызывается GET /api/v1/events/{id} со значением id = 1.
     * 2. Проверяется, что в результате запроса получается статус 404 Not Found.
     * 3. Сверяется, что в ответе отправляется соответствующая ошибка.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void findByIdNotFound() throws Exception {

        long id = 1L;

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/" + id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound())
                .andReturn();

        ErrorDTO errorDTO = getResponse(result, ErrorDTO.class);

        Assertions.assertThat(errorDTO.getErrorType()).isEqualTo(EVENT_NOT_FOUND);
        Assertions.assertThat(errorDTO.getMessage()).isEqualTo(EVENT_NOT_FOUND.getDescription());

    }

    /**
     * Интеграционный тест для проверки отрицательного сценария записи на мероприятие, когда пользователь уже записан на мероприятие.
     * <p>
     * В тесте:
     * 1. Создается мероприятие.
     * 2. Создается пользователь и добавляется в список участников на мероприятии.
     * 3. Вызывается POST /api/v1/events/{id}/participants со значением id = 1.
     * 4. Проверяется, что в результате запроса получается статус 400 Bad Request.
     * 5. Сверяется, что в ответе отправляется соответствующая ошибка.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void addParticipantUserAlreadyEnrolled() throws Exception {

        Event event = createEvent();

        User user = User.builder()
                .login(TEST_USER)
                .password("1")
                .build();
        userRepository.save(user);

        event.getParticipants().add(user);
        eventRepository.save(event);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL + "/" + event.getId().toString() + "/participants"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest())
                .andReturn();

        ErrorDTO errorDTO = getResponse(result, ErrorDTO.class);

        Assertions.assertThat(errorDTO.getErrorType()).isEqualTo(USER_ALREADY_ENROLLED);
        Assertions.assertThat(errorDTO.getMessage()).isEqualTo(USER_ALREADY_ENROLLED.getDescription());
    }

    /**
     * Интеграционный тест для проверки успешной записи на мероприятие.
     * <p>
     * В тесте:
     * 1. Создается мероприятие.
     * 2. Создается пользователь.
     * 3. Вызывается POST /api/v1/events/{id}/participants со значением id = 1.
     * 4. Проверяется, что в результате запроса получается статус 200 OK.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void addParticipantSuccess() throws Exception {

        Event event = createEvent();

        User user = User.builder()
                .login(TEST_USER)
                .password("1")
                .build();
        userRepository.save(user);

        mvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL + "/" + event.getId().toString() + "/participants"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());

    }

    /**
     * Интеграционный тест для проверки успешной отмены записи на мероприятие.
     * <p>
     * В тесте:
     * 1. Создается мероприятие.
     * 2. Создается пользователь и добавляется в список участников мероприятия.
     * 3. Вызывается DELETE /api/v1/events/{id}/participants со значением id = 1.
     * 4. Проверяется, что в результате запроса получается статус 200 OK.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void deleteParticipantSuccess() throws Exception {

        Event event = createEvent();

        User user = User.builder()
                .login(TEST_USER)
                .password("1")
                .build();
        userRepository.save(user);

        event.getParticipants().add(user);
        eventRepository.save(event);

        mvc.perform(MockMvcRequestBuilders
                        .delete(BASE_URL + "/" + event.getId().toString() + "/participants"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());

    }

    /**
     * Интеграционный тест для проверки отрицательного сценария записи на мероприятие, когда пользователь уже записан на мероприятие.
     * <p>
     * В тесте:
     * 1. Создается мероприятие.
     * 2. Создается пользователь и добавляется в список участников на мероприятии.
     * 3. Вызывается POST /api/v1/events/{id}/participants со значением id = 1.
     * 4. Проверяется, что в результате запроса получается статус 400 Bad Request.
     * 5. Сверяется, что в ответе отправляется соответствующая ошибка.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void deleteParticipantCurrentUserNotEnrolled() throws Exception {

        Event event = createEvent();

        User user = User.builder()
                .login(TEST_USER)
                .password("1")
                .build();
        userRepository.save(user);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .delete(BASE_URL + "/" + event.getId().toString() + "/participants"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest())
                .andReturn();

        ErrorDTO errorDTO = getResponse(result, ErrorDTO.class);

        Assertions.assertThat(errorDTO.getErrorType()).isEqualTo(CURRENT_USER_NOT_ENROLLED);
        Assertions.assertThat(errorDTO.getMessage()).isEqualTo(CURRENT_USER_NOT_ENROLLED.getDescription());

    }

    private Event createEvent() {

        List<WorkingHours> workingHoursList = new ArrayList<>();
        workingHoursList
                .add(WorkingHours.builder()
                        .dayOfWeek(FRIDAY)
                        .startTime(LocalTime.of(10, 00))
                        .endTime(LocalTime.of(16, 00))
                        .build());

        Place place = Place.builder()
                .name("Стадион")
                .address("ул. Ленина, д. 1")
                .workingHoursList(workingHoursList)
                .build();

        for (WorkingHours workingHours : workingHoursList)
            workingHours.setPlace(place);

        place = placeRepository.save(place);

        Set<User> participants = new HashSet<>();

        Event event = Event.builder()
                .name("Баскетбол для любителей")
                .time(LocalDateTime.of(2021, 12, 21, 12, 00))
                .duration(120)
                .placeId(place.getId())
                .place(place)
                .maxNumberOfParticipants(12)
                .description("Какое-то мероприятие")
                .category(BASKETBALL)
                .participants(participants)
                .build();
        event = eventRepository.save(event);

        return event;

    }

}
