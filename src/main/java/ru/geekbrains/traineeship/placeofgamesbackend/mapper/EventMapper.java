package ru.geekbrains.traineeship.placeofgamesbackend.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.event.EventDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.event.EventDetailsDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.event.EventToCreateDTO;
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

    public Event mapToEvent(EventToCreateDTO eventDTO) {
        return Event.builder()
                .name(eventDTO.getName())
                .time(eventDTO.getTime())
                .duration(eventDTO.getDuration())
                .placeId(eventDTO.getPlaceId())
                .maxNumberOfParticipants(eventDTO.getMaxNumberOfParticipants())
                .description(eventDTO.getDescription())
                .category(eventDTO.getCategory())
                .build();
    }

}
