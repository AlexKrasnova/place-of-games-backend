package ru.geekbrains.traineeship.placeofgamesbackend.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.EventDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.EventDetailsDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final PlaceMapper placeMapper;
    private final UserMapper userMapper;

    public EventDTO mapToEventDTO(Event event, String currentUserLogin) {
        return EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .time(event.getTime())
                .duration(event.getDuration())
                .place(placeMapper.mapToPlaceDTO(event.getPlace()))
                .maxNumberOfParticipants(event.getMaxNumberOfParticipants())
                .numberOfParticipants(event.getParticipants().size())
                .category(event.getCategory())
                .isCurrentUserEnrolled(event
                        .getParticipants()
                        .stream()
                        .anyMatch(user -> user.getLogin().equals(currentUserLogin))
                )
                .build();
    }

    public EventDetailsDTO mapToEventDetailsDTO(Event event, String currentUserLogin) {
        return EventDetailsDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .time(event.getTime())
                .duration(event.getDuration())
                .place(placeMapper.mapToPlaceDTO(event.getPlace()))
                .maxNumberOfParticipants(event.getMaxNumberOfParticipants())
                .numberOfParticipants(event.getParticipants().size())
                .description(event.getDescription())
                .category(event.getCategory())
                .isCurrentUserEnrolled(event
                        .getParticipants()
                        .stream()
                        .anyMatch(user -> user.getLogin().equals(currentUserLogin))
                )
                .participants(event.getParticipants().stream().map(userMapper::mapToUserDTO).collect(Collectors.toSet()))
                .build();
    }

}
