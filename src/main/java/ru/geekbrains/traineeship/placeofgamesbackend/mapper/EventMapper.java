package ru.geekbrains.traineeship.placeofgamesbackend.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.EventWithPlaceDetailsDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final PlaceMapper placeMapper;

    public EventWithPlaceDetailsDTO convertEventToEventWithPlaceDetailsDTO(Event event) {
        return EventWithPlaceDetailsDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .time(event.getTime())
                .duration(event.getDuration())
                .place(placeMapper.convertPlaceToPlaceDTO(event.getPlace()))
                .maxNumberOfParticipants(event.getMaxNumberOfParticipants())
                .numberOfParticipants(event.getNumberOfParticipants())
                .build();
    }

}
