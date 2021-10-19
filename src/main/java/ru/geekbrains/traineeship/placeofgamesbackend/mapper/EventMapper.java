package ru.geekbrains.traineeship.placeofgamesbackend.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.EventDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final PlaceMapper placeMapper;

    public EventDTO mapToEventDTO(Event event, String currentUser) {
        return EventDTO.builder()
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
                        .anyMatch(user -> user.getLogin().equals(currentUser))
                )
                .build();
    }

}
