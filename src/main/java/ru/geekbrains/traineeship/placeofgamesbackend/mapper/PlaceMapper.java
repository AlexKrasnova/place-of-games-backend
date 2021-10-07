package ru.geekbrains.traineeship.placeofgamesbackend.mapper;

import org.springframework.stereotype.Component;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.PlaceDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Place;

@Component
public class PlaceMapper {

    public PlaceDTO convertPlaceToPlaceDTO(Place place) {
        return PlaceDTO.builder()
                .id(place.getId())
                .name(place.getName())
                .address(place.getAddress())
                .build();
    }
}
