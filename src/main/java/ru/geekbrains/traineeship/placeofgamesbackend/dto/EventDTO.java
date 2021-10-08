package ru.geekbrains.traineeship.placeofgamesbackend.dto;

import lombok.Builder;
import lombok.Data;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
public class EventDTO {

    private Long id;
    private String name;
    private LocalDateTime time;
    private Integer duration;
    private PlaceDTO place;
    private Integer maxNumberOfParticipants;
    private Integer numberOfParticipants;

}
