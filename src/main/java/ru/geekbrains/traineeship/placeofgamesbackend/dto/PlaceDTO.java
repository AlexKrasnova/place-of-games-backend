package ru.geekbrains.traineeship.placeofgamesbackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaceDTO {

    private Long id;

    private String name;

    private String address;

}
