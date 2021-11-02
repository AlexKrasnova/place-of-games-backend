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
import ru.geekbrains.traineeship.placeofgamesbackend.dto.ErrorDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.TimePeriodDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Place;
import ru.geekbrains.traineeship.placeofgamesbackend.model.WorkingHours;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.EventRepository;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.PlaceRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ru.geekbrains.traineeship.placeofgamesbackend.dto.ErrorType.PLACE_NOT_WORKING;

public class PlaceControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String BASE_URL = "/api/v1/places";

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private EventRepository eventRepository;

    @AfterEach
    public void tearDown() {
        eventRepository.deleteAll();
        placeRepository.deleteAll();
    }

    /**
     * Интеграционный тест на проверку успешного поиска свободного времени на площадке на выбранную дату,
     * когда никаких мероприятий на эту дату не запланированно.
     * <p>
     * В тесте:
     * 1. Создается создается и добавляется в базу данных площадка с режимом работы.
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

        Assertions.assertThat(actual.get(0).getStartTime()).isEqualTo(date.atTime(place.getWorkingHoursList().get(0).getStartTime()));
        Assertions.assertThat(actual.get(0).getEndTime()).isEqualTo(date.plusDays(1).atTime(place.getWorkingHoursList().get(0).getEndTime()));

    }

    /**
     * Интеграционный тест на проверку успешного поиска свободного времени на площадке на выбранную дату,
     * когда на эту дату запланированны несколько мероприятий.
     * <p>
     * В тесте:
     * 1. Создается создается и добавляется в бд площадка с режимом работы.
     * 2. Создается и добавляется в бд несколько мероприятий на этой площадки в выбранный день.
     * 2. Вызывается GET /api/v1/places/{id}/free-time c id созданной площадки.
     * 3. Проверяется, что в результате запроса получается статус 200 OK и сверяется результат -
     * свободное время соответствует времени, не занятому созданными мероприятиями.
     */
    @WithMockUser
    @Test
    public void getFreeTimeSeveralEventsOnDateSuccess() throws Exception {

        LocalDate date = LocalDate.of(2021, 11, 5);

        Place place = createPlace();

        Event event1 = Event.builder()
                .name("Теннис")
                .time(date.atTime(10, 30))
                .duration(90)
                .placeId(place.getId())
                .place(place)
                .build();

        eventRepository.save(event1);

        Event event2 = Event.builder()
                .name("Футбол")
                .time(date.atTime(14, 00))
                .duration(120)
                .placeId(place.getId())
                .place(place)
                .build();

        eventRepository.save(event2);

        Event event3 = Event.builder()
                .name("Футбол")
                .time(date.atTime(16, 00))
                .duration(90)
                .placeId(place.getId())
                .place(place)
                .build();

        eventRepository.save(event3);

        Event event4 = Event.builder()
                .name("Футбол")
                .time(date.plusDays(1).atTime(00, 30))
                .duration(90)
                .placeId(place.getId())
                .place(place)
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

        Assertions.assertThat(actual.get(0).getStartTime()).isEqualTo(date.atTime(place.getWorkingHoursList().get(0).getStartTime()));
        Assertions.assertThat(actual.get(0).getEndTime()).isEqualTo(event1.getTime());

        Assertions.assertThat(actual.get(1).getStartTime()).isEqualTo(event1.getTime().plusMinutes(event1.getDuration()));
        Assertions.assertThat(actual.get(1).getEndTime()).isEqualTo(event2.getTime());

        Assertions.assertThat(actual.get(2).getStartTime()).isEqualTo(event3.getTime().plusMinutes(event1.getDuration()));
        Assertions.assertThat(actual.get(2).getEndTime()).isEqualTo(event4.getTime());

    }

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
                        .isBadRequest())
                .andReturn();

        ErrorDTO errorDTO = getResponse(result, ErrorDTO.class);

        Assertions.assertThat(errorDTO.getErrorType()).isEqualTo(PLACE_NOT_WORKING);
        Assertions.assertThat(errorDTO.getMessage()).isEqualTo(PLACE_NOT_WORKING.getDescription());


    }

    private Place createPlace() {

        List<WorkingHours> workingHoursList = new ArrayList<>();

        workingHoursList
                .add(WorkingHours.builder()
                        .dayOfWeek(DayOfWeek.FRIDAY)
                        .startTime(LocalTime.of(10, 00))
                        .endTime(LocalTime.of(2, 00))
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
