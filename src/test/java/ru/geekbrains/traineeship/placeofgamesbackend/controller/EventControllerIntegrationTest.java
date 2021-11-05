package ru.geekbrains.traineeship.placeofgamesbackend.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.geekbrains.traineeship.placeofgamesbackend.AbstractIntegrationTest;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.error.ErrorDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.event.EventDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.event.EventToCreateDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.place.PlaceDTO;
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

import static ru.geekbrains.traineeship.placeofgamesbackend.dto.error.ErrorType.*;
import static ru.geekbrains.traineeship.placeofgamesbackend.model.Category.BASKETBALL;
import static ru.geekbrains.traineeship.placeofgamesbackend.model.Category.TENNIS;


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

        User user = createUser(TEST_USER);

        Event event = createEvent();

        event.getParticipants().add(user);
        event.setOwnerId(user.getId());
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

        User user = createUser(TEST_USER);

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

        User user = createUser(TEST_USER);

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

    /**
     * Интеграционный тест для проверки успешного добавления мероприятия,
     * когда на эту дату на данной площадке уже есть мероприятие,
     * но его время не совпадает с временем нового мероприятия.
     * <p>
     * В тесте:
     * 1. Создается площадка, мероприятие, пользователь.
     * 2. Создается объект EventToCreateDTO с такой же датой, как у уже созданного мероприятия,
     * но не пересекающимся с ним временем, который парсится в json.
     * 3. Вызывается POST /api/v1/events, в теле отправляется созданный json объект.
     * 4. Проверяется, что в результате запроса получается статус 201 Created.
     * 5. В бд находится мероприятие с id = 2.
     * 6. Сверяется, что значения его полей соответсвтуют значеням полей созданного нами мероприятия.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void createSuccess() throws Exception {

        Event anotherEvent = createEvent();

        User user = createUser(TEST_USER);

        EventToCreateDTO eventDTO = EventToCreateDTO.builder()
                .name("Теннис")
                .time(anotherEvent.getTime().minusMinutes(120))
                .duration(120)
                .placeId(anotherEvent.getPlace().getId())
                .category(TENNIS)
                .description("Просто игра")
                .maxNumberOfParticipants(4)
                .build();

        mvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated());

        List<Event> events = eventRepository.findAll();
        Event event = events.get(1);

        Assertions.assertThat(event.getName()).isEqualTo(eventDTO.getName());
        Assertions.assertThat(event.getTime()).isEqualTo(eventDTO.getTime());
        Assertions.assertThat(event.getDuration()).isEqualTo(eventDTO.getDuration());
        Assertions.assertThat(event.getDescription()).isEqualTo(eventDTO.getDescription());
        Assertions.assertThat(event.getPlaceId()).isEqualTo(eventDTO.getPlaceId());
        Assertions.assertThat(event.getCategory()).isEqualTo(eventDTO.getCategory());
        Assertions.assertThat(event.getMaxNumberOfParticipants()).isEqualTo(eventDTO.getMaxNumberOfParticipants());
        Assertions.assertThat(event.getOwnerId()).isEqualTo(user.getId());

    }

    /**
     * Интеграционный тест для проверки отрицательного сценария добавления нового мероприятия,
     * площадка не работает в выбранное время.
     * <p>
     * В тесте:
     * 1. Создается площадка и пользователь.
     * 2. Создается объект EventToCreateDTO с датой, в которую площадка не работает.
     * 3. Вызывается POST /api/v1/events, в теле отправляется созданный json объект.
     * 4. Проверяется, что в результате запроса получается статус 400 Bad Request.
     * 5. Сверяется, что в ответе отправляется соответствующая ошибка.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void createNotWorkingTimePeriod() throws Exception {

        Place place = createPlaceWithWorkingHours();

        createUser(TEST_USER);

        EventToCreateDTO eventDTO = EventToCreateDTO.builder()
                .name("Теннис")
                .time(LocalDateTime.of(2021, 11, 13, 10, 0))
                .duration(120)
                .placeId(place.getId())
                .category(TENNIS)
                .description("Просто игра")
                .maxNumberOfParticipants(4)
                .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest())
                .andReturn();

        ErrorDTO errorDTO = getResponse(result, ErrorDTO.class);

        Assertions.assertThat(errorDTO.getErrorType()).isEqualTo(NOT_WORKING_OR_NOT_FREE_TIME_PERIOD);
        Assertions.assertThat(errorDTO.getMessage()).isEqualTo(NOT_WORKING_OR_NOT_FREE_TIME_PERIOD.getDescription());

    }

    /**
     * Интеграционный тест для проверки отрицательного сценария добавления нового мероприятия,
     * время мероприятия пересекается с уже существующим мероприятием.
     * <p>
     * В тесте:
     * 1. Создается площадка, мероприятие и пользователь.
     * 2. Создается объект EventToCreateDTO время которого пересекается с временем уже существующего мероприятия.
     * 3. Вызывается POST /api/v1/events, в теле отправляется созданный json объект.
     * 4. Проверяется, что в результате запроса получается статус 400 Bad Request.
     * 5. Сверяется, что в ответе отправляется соответствующая ошибка.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void createNotFreeTimePeriod() throws Exception {

        Event event = createEvent();

        createUser(TEST_USER);

        EventToCreateDTO eventDTO = EventToCreateDTO.builder()
                .name("Теннис")
                .time(event.getTime().minusMinutes(60))
                .duration(120)
                .placeId(event.getPlace().getId())
                .category(TENNIS)
                .description("Просто игра")
                .maxNumberOfParticipants(4)
                .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest())
                .andReturn();

        ErrorDTO errorDTO = getResponse(result, ErrorDTO.class);

        Assertions.assertThat(errorDTO.getErrorType()).isEqualTo(NOT_WORKING_OR_NOT_FREE_TIME_PERIOD);
        Assertions.assertThat(errorDTO.getMessage()).isEqualTo(NOT_WORKING_OR_NOT_FREE_TIME_PERIOD.getDescription());

    }

    private User createUser(String login) {

        User user = User.builder()
                .login(login)
                .name("name")
                .password("1")
                .build();

        return userRepository.save(user);
    }

    private Event createEvent() {

        User user = createUser(TEST_USER + 1);

        Place place = createPlaceWithWorkingHours();

        Set<User> participants = new HashSet<>();

        Event event = Event.builder()
                .name("Баскетбол для любителей")
                .time(LocalDateTime.of(2021, 11, 12, 12, 0))
                .duration(120)
                .placeId(place.getId())
                .place(place)
                .maxNumberOfParticipants(12)
                .description("Какое-то мероприятие")
                .category(BASKETBALL)
                .participants(participants)
                .ownerId(user.getId())
                .build();
        event = eventRepository.save(event);

        return event;

    }

    private Place createPlaceWithWorkingHours() {
        List<WorkingHours> workingHoursList = new ArrayList<>();
        workingHoursList
                .add(WorkingHours.builder()
                        .dayOfWeek(DayOfWeek.FRIDAY)
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

        return placeRepository.save(place);
    }

}
