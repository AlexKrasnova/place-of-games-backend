package ru.geekbrains.traineeship.placeofgamesbackend.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.place.PlaceDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.user.UserDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Category;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class EventDetailsDTO {

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

    /**
     * Признак того, является ли текущий пользователь owner-ом мероприятия
     */
    private Boolean isCurrentUserOwner;

    private Set<UserDTO> participants;

}
