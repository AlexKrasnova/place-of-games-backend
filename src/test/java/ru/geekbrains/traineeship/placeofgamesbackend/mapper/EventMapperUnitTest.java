package ru.geekbrains.traineeship.placeofgamesbackend.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.event.EventDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.event.EventDetailsDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.place.PlaceDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.user.UserDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Place;
import ru.geekbrains.traineeship.placeofgamesbackend.model.User;
import ru.geekbrains.traineeship.placeofgamesbackend.model.WorkingHours;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.geekbrains.traineeship.placeofgamesbackend.model.Category.BASKETBALL;

@ExtendWith(MockitoExtension.class)
public class EventMapperUnitTest {

    @Mock
    private PlaceMapper placeMapper;

    @Mock
    private UserMapper userMapper;

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

        String currentUserLogin = "user";

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

        User user = User.builder()
                .id(1L)
                .name("Вася")
                .login("user1")
                .password("1")
                .build();
        Set<User> participants = Collections.singleton(user);

        Event event = Event.builder()
                .name("Баскетбол для любителей")
                .time(LocalDateTime.of(2021, 12, 21, 12, 0))
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
                        .build())
                .when(placeMapper).mapToPlaceDTO(place);

        EventDTO result = eventMapper.mapToEventDTO(event, currentUserLogin);
        Assertions.assertThat(result.getId()).isEqualTo(event.getId());
        Assertions.assertThat(result.getName()).isEqualTo(event.getName());
        Assertions.assertThat(result.getTime()).isEqualTo(event.getTime());
        Assertions.assertThat(result.getDuration()).isEqualTo(event.getDuration());
        Assertions.assertThat(result.getPlace()).isEqualTo(placeMapper.mapToPlaceDTO(place));
        Assertions.assertThat(result.getMaxNumberOfParticipants()).isEqualTo(event.getMaxNumberOfParticipants());
        Assertions.assertThat(result.getCategory()).isEqualTo(event.getCategory());
        Assertions.assertThat(result.getIsCurrentUserEnrolled()).isFalse();
    }

    /**
     * Unit-тест на проверку корректной работы метода mapToEventDTO в маппере мероприятий.
     * <p>
     * В тесте:
     * 1. Создается Event event и EventDTO eventDTO с соответствующими значениями полей.
     * 2. Мочится метод mapToPlaceDTO в маппере площадок.
     * 3. Проверяется, что результат вызова метода mapToEventDTO в маппере мероприятий совпадает с созданным в тесте eventDTO.
     */
    @Test
    public void mapToEventDetailsDTOSuccess() {

        String currentUserLogin = "user";

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

        User user = User.builder()
                .id(1L)
                .name("Вася")
                .login("user1")
                .password("1")
                .build();
        Set<User> participants = Collections.singleton(user);

        Event event = Event.builder()
                .name("Баскетбол для любителей")
                .time(LocalDateTime.of(2021, 12, 21, 12, 0))
                .duration(120)
                .placeId(place.getId())
                .place(place)
                .maxNumberOfParticipants(12)
                .description("Какое-то мероприятие")
                .category(BASKETBALL)
                .participants(participants)
                .ownerId(user.getId())
                .owner(user)
                .build();

        Mockito.doReturn(PlaceDTO.builder()
                        .id(place.getId())
                        .name(place.getName())
                        .address(place.getAddress())
                        .build())
                .when(placeMapper).mapToPlaceDTO(place);

        Mockito.doReturn(UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .build())
                .when(userMapper).mapToUserDTO(user);

        EventDetailsDTO result = eventMapper.mapToEventDetailsDTO(event, currentUserLogin);
        Assertions.assertThat(result.getId()).isEqualTo(event.getId());
        Assertions.assertThat(result.getName()).isEqualTo(event.getName());
        Assertions.assertThat(result.getTime()).isEqualTo(event.getTime());
        Assertions.assertThat(result.getDuration()).isEqualTo(event.getDuration());
        Assertions.assertThat(result.getPlace()).isEqualTo(placeMapper.mapToPlaceDTO(place));
        Assertions.assertThat(result.getMaxNumberOfParticipants()).isEqualTo(event.getMaxNumberOfParticipants());
        Assertions.assertThat(result.getCategory()).isEqualTo(event.getCategory());
        Assertions.assertThat(result.getDescription()).isEqualTo(event.getDescription());
        Assertions.assertThat(result.getIsCurrentUserEnrolled()).isFalse();
        Assertions.assertThat(result.getIsCurrentUserOwner()).isFalse();
        Assertions.assertThat(result.getParticipants()).isEqualTo(event.getParticipants().stream().map(userMapper::mapToUserDTO).collect(Collectors.toSet()));

    }
}
