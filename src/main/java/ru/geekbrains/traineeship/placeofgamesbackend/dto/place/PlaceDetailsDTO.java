package ru.geekbrains.traineeship.placeofgamesbackend.dto.place;

import lombok.Builder;
import lombok.Data;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.WorkingHoursDTO;

import java.util.List;

@Data
@Builder
public class PlaceDetailsDTO {

    private Long id;

    private String name;

    private String address;

    private String description;

    private List<WorkingHoursDTO> workingHoursList;

}
