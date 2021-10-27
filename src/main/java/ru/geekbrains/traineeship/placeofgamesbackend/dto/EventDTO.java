package ru.geekbrains.traineeship.placeofgamesbackend.dto;

import lombok.Builder;
import lombok.Data;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Category;

import java.time.LocalDateTime;
import java.util.Set;

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

    private String description;

    private Category category;

    /**
     * Признак того, что текущий пользователь уже записан на данное мероприятие
     */
    private Boolean isCurrentUserEnrolled;

    private Set<UserDTO> participants;
}
