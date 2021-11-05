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
import ru.geekbrains.traineeship.placeofgamesbackend.dto.TimePeriodDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Place;
import ru.geekbrains.traineeship.placeofgamesbackend.model.User;
import ru.geekbrains.traineeship.placeofgamesbackend.model.WorkingHours;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.EventRepository;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.PlaceRepository;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.UserRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PlaceControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String BASE_URL = "/api/v1/places";

    private static final String TEST_USER = "testUser";

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        eventRepository.deleteAll();
        placeRepository.deleteAll();
        userRepository.deleteAll();
    }

    /**
     * Интеграционный тест на проверку успешного поиска свободного времени на площадке на выбранную дату,
     * когда никаких мероприятий на эту дату не запланировано.
     * <p>
     * В тесте:
     * 1. Создается и добавляется в базу данных площадка с режимом работы.
     * 2. Вызывается GET /api/v1/places/{id}/free-time c id созданной площадки.
     * 3. Проверяется, что в результате запроса получается статус 200 OK и сверяется результат -
     * свободное время соответствует времени работы площадки в выбранный день.
     */
    @WithMockUser
    @Test
    public void getFreeTimeNoEventsOnDateSuccess() throws Exception {

        LocalDate date = LocalDate.of(2021, 11, 5);

        Place place = createPlace();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/" + place.getId() + "/free-time?date=" + date))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        List<TimePeriodDTO> actual = getResponse(result, new TypeReference<>() {
        });

        Assertions.assertThat(actual.size()).isEqualTo(1);
        Assertions.assertThat(actual.get(0).getStartTime()).isEqualTo(date.atTime(place.getWorkingHoursList().get(0).getStartTime()));
        Assertions.assertThat(actual.get(0).getEndTime()).isEqualTo(date.plusDays(1).atTime(place.getWorkingHoursList().get(0).getEndTime()));

    }

    /**
     * Интеграционный тест на проверку успешного поиска свободного времени на площадке на выбранную дату,
     * когда на эту дату запланированы несколько мероприятий.
     * <p>
     * В тесте:
     * 1. Создается и добавляется в бд площадка с режимом работы.
     * 2. Создается и добавляется в бд несколько мероприятий на этой площадке в выбранный день.
     * 3. Вызывается GET /api/v1/places/{id}/free-time c id созданной площадки.
     * 4. Проверяется, что в результате запроса получается статус 200 OK и сверяется результат -
     * свободное время соответствует времени работы площадки, не занятому созданными мероприятиями.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void getFreeTimeSeveralEventsOnDateSuccess() throws Exception {

        LocalDate date = LocalDate.of(2021, 11, 5);

        Place place = createPlace();

        User user = User.builder()
                .login(TEST_USER)
                .name("Вася")
                .build();
        userRepository.save(user);

        Event event1 = Event.builder()
                .name("Теннис")
                .time(date.atTime(10, 30))
                .duration(90)
                .placeId(place.getId())
                .place(place)
                .ownerId(user.getId())
                .build();

        eventRepository.save(event1);


        Event event3 = Event.builder()
                .name("Футбол")
                .time(date.atTime(16, 0))
                .duration(90)
                .placeId(place.getId())
                .place(place)
                .ownerId(user.getId())
                .build();

        eventRepository.save(event3);

        Event event2 = Event.builder()
                .name("Футбол")
                .time(date.atTime(14, 0))
                .duration(120)
                .placeId(place.getId())
                .place(place)
                .ownerId(user.getId())
                .build();

        eventRepository.save(event2);
        Event event4 = Event.builder()
                .name("Футбол")
                .time(date.plusDays(1).atTime(0, 30))
                .duration(90)
                .placeId(place.getId())
                .place(place)
                .ownerId(user.getId())
                .build();

        eventRepository.save(event4);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/" + place.getId() + "/free-time?date=" + date))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        List<TimePeriodDTO> actual = getResponse(result, new TypeReference<>() {
        });

        Assertions.assertThat(actual.size()).isEqualTo(3);

        Assertions.assertThat(actual.get(0).getStartTime()).isEqualTo(date.atTime(place.getWorkingHoursList().get(0).getStartTime()));
        Assertions.assertThat(actual.get(0).getEndTime()).isEqualTo(event1.getTime());

        Assertions.assertThat(actual.get(1).getStartTime()).isEqualTo(event1.getTime().plusMinutes(event1.getDuration()));
        Assertions.assertThat(actual.get(1).getEndTime()).isEqualTo(event2.getTime());

        Assertions.assertThat(actual.get(2).getStartTime()).isEqualTo(event3.getTime().plusMinutes(event1.getDuration()));
        Assertions.assertThat(actual.get(2).getEndTime()).isEqualTo(event4.getTime());

    }

    /**
     * Интеграционный тест на проверку успешного поиска свободного времени на площадке на выбранную дату,
     * когда на эту дату запланирован одно мероприятие, а у площадки есть несколько интервалов рабочего времени.
     * <p>
     * В тесте:
     * 1. Создается и добавляется в бд площадка с режимом работы.
     * 2. Добавляется режим работы на другой день, состоящий из двух интервалов.
     * 3. Создается и добавляется в бд одно мероприятие на этой площадке в выбранный день.
     * 2. Вызывается GET /api/v1/places/{id}/free-time c id созданной площадки.
     * 3. Проверяется, что в результате запроса получается статус 200 OK и сверяется результат -
     * свободное время соответствует времени, не занятому созданными мероприятиями.
     */

    @WithMockUser(value = TEST_USER)
    @Test
    public void getFreeTimeSeveralWorkingPeriodsOnDateSuccess() throws Exception {

        User user = User.builder()
                .login(TEST_USER)
                .name("Вася")
                .build();
        userRepository.save(user);

        LocalDate date = LocalDate.of(2021, 11, 6);

        Place place = createPlace();

        place.getWorkingHoursList().add(WorkingHours.builder()
                .dayOfWeek(DayOfWeek.SATURDAY)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(14, 0))
                .place(place)
                .placeId(place.getId())
                .build());

        place.getWorkingHoursList().add(WorkingHours.builder()
                .dayOfWeek(DayOfWeek.SATURDAY)
                .startTime(LocalTime.of(15, 0))
                .endTime(LocalTime.of(22, 0))
                .place(place)
                .placeId(place.getId())
                .build());


        placeRepository.save(place);

        Event event1 = Event.builder()
                .name("Теннис")
                .time(date.atTime(10, 30))
                .duration(90)
                .placeId(place.getId())
                .place(place)
                .ownerId(user.getId())
                .build();

        eventRepository.save(event1);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/" + place.getId() + "/free-time?date=" + date))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        List<TimePeriodDTO> actual = getResponse(result, new TypeReference<>() {
        });

        Assertions.assertThat(actual.size()).isEqualTo(3);

        Assertions.assertThat(actual.get(0).getStartTime()).isEqualTo(date.atTime(place.getWorkingHoursList().get(1).getStartTime()));
        Assertions.assertThat(actual.get(0).getEndTime()).isEqualTo(event1.getTime());

        Assertions.assertThat(actual.get(1).getStartTime()).isEqualTo(event1.getTime().plusMinutes(event1.getDuration()));
        Assertions.assertThat(actual.get(1).getEndTime()).isEqualTo(date.atTime(place.getWorkingHoursList().get(1).getEndTime()));

        Assertions.assertThat(actual.get(2).getStartTime()).isEqualTo(date.atTime(place.getWorkingHoursList().get(2).getStartTime()));
        Assertions.assertThat(actual.get(2).getEndTime()).isEqualTo(date.atTime(place.getWorkingHoursList().get(2).getEndTime()));

    }

    /**
     * Интеграционный тест на проверку поиска свободного времени на площадке,
     * когда площадка не работает в указанный день.
     * <p>
     * В тесте:
     * 1. Создается и добавляется в бд площадка с режимом работы.
     * 2. Вызывается GET /api/v1/places/{id}/free-time c id созданной площадки и датой, когда площадка не работает.
     * 3. Проверяется, что в результате запроса получается статус 200 OK и в ответе приходит пустой список.
     */
    @WithMockUser
    @Test
    public void getFreeTimePlaceNotWorking() throws Exception {

        Place place = createPlace();

        LocalDate date = LocalDate.of(2021, 11, 6);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/" + place.getId() + "/free-time?date=" + date))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        List<TimePeriodDTO> actual = getResponse(result, new TypeReference<>() {
        });

        Assertions.assertThat(actual).isEmpty();

    }

    private Place createPlace() {

        List<WorkingHours> workingHoursList = new ArrayList<>();

        workingHoursList
                .add(WorkingHours.builder()
                        .dayOfWeek(DayOfWeek.FRIDAY)
                        .startTime(LocalTime.of(10, 0))
                        .endTime(LocalTime.of(2, 0))
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
