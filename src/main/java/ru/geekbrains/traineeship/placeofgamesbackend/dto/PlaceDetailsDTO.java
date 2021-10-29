package ru.geekbrains.traineeship.placeofgamesbackend.dto;

import lombok.Builder;
import lombok.Data;

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
