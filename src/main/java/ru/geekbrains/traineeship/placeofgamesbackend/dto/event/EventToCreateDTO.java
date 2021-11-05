package ru.geekbrains.traineeship.placeofgamesbackend.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Category;

import java.time.LocalDateTime;

@Data
@Builder
public class EventToCreateDTO {

    private String name;

    private LocalDateTime time;

    private Integer duration;

    private Long placeId;

    private Integer maxNumberOfParticipants;

    private String description;

    private Category category;


}
