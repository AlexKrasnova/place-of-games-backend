package ru.geekbrains.traineeship.placeofgamesbackend.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.EventDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.PlaceDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.WorkingHoursDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Place;
import ru.geekbrains.traineeship.placeofgamesbackend.model.User;
import ru.geekbrains.traineeship.placeofgamesbackend.model.WorkingHours;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static ru.geekbrains.traineeship.placeofgamesbackend.model.Category.BASKETBALL;
import static ru.geekbrains.traineeship.placeofgamesbackend.model.DayOfWeek.FRIDAY;

@ExtendWith(MockitoExtension.class)
public class EventMapperUnitTest {

    @Mock
    private PlaceMapper placeMapper;

    @InjectMocks
    private EventMapper eventMapper;

    /**
     * Unit-тест на проверку корректной работы метода mapToEventDTO в маппере мероприятий.
     * <p>
     * В тесте:
     * 1. Создается Event event и EventDTO eventDTO с соответствующими значениями полей.
     * 2. Мочится метод mapToPlaceDTO в маппере площадок.
     * 3. Проверяется, что результат вызова метода mapToEventDTO в маппере мероприятий совпадает с созданным в тесте eventDTO.
     */
    @Test
    public void mapToEventDTOSuccess() {

        String currentUser = "user";

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

        Set<User> participants = Collections.emptySet();

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

        Mockito.doReturn(PlaceDTO.builder()
                .id(place.getId())
                .name(place.getName())
                .address(place.getAddress())
                .workingHoursList(Collections.singletonList(WorkingHoursDTO.builder()
                        .dayOfWeek(workingHoursList.get(0).getDayOfWeek())
                        .date(workingHoursList.get(0).getDate())
                        .startTime(workingHoursList.get(0).getStartTime())
                        .endTime(workingHoursList.get(0).getEndTime())
                        .build()))
                .build())
        .when(placeMapper).mapToPlaceDTO(place);

        EventDTO eventDTO = EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .time(event.getTime())
                .duration(event.getDuration())
                .place(placeMapper.mapToPlaceDTO(place))
                .maxNumberOfParticipants(event.getMaxNumberOfParticipants())
                .description(event.getDescription())
                .category(event.getCategory())
                .numberOfParticipants(event.getParticipants().size())
                .isCurrentUserEnrolled(false)
                .build();

        Assertions.assertThat(eventMapper.mapToEventDTO(event, currentUser)).isEqualTo(eventDTO);

    }
}
