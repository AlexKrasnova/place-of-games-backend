package ru.geekbrains.traineeship.placeofgamesbackend.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.EventDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final PlaceMapper placeMapper;

    public EventDTO mapToEventDTO(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .time(event.getTime())
                .duration(event.getDuration())
                .place(placeMapper.mapToPlaceDTO(event.getPlace()))
                .maxNumberOfParticipants(event.getMaxNumberOfParticipants())
                .numberOfParticipants(event.getNumberOfParticipants())
                .build();
    }

}
