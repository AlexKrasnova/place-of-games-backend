package ru.geekbrains.traineeship.placeofgamesbackend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Place;
import ru.geekbrains.traineeship.placeofgamesbackend.model.WorkingHours;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.EventRepository;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.PlaceRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ru.geekbrains.traineeship.placeofgamesbackend.model.Category.BASKETBALL;
import static ru.geekbrains.traineeship.placeofgamesbackend.model.DayOfWeek.FRIDAY;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerIntegrationTest {

    private static final String BASE_URL = "/api/v1/events";

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private MockMvc mvc;

    @WithMockUser(value = "spring")
    @Test
    public void findAllSuccess() throws Exception {

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
        place = placeRepository.save(place);

        Event event = Event.builder()
                .name("Баскетбол для любителей")
                .time(LocalDateTime.of(2021, 12, 21, 12, 00))
                .duration(120)
                .placeId(place.getId())
                .maxNumberOfParticipants(12)
                .category(BASKETBALL)
                .build();
        event = eventRepository.save(event);

        mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
