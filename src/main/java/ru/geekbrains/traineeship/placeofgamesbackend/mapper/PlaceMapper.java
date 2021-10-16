package ru.geekbrains.traineeship.placeofgamesbackend.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.PlaceDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Place;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlaceMapper {

    private final WorkingHoursMapper workingHoursMapper;

    public PlaceDTO mapToPlaceDTO(Place place) {
        return PlaceDTO.builder()
                .id(place.getId())
                .name(place.getName())
                .address(place.getAddress())
                .description(place.getDescription())
                .workingHoursList(place.getWorkingHoursList()
                        .stream()
                        .map(workingHoursMapper::mapToWorkingHoursDTO)
                        .collect(Collectors.toList()))
                .build();
    }
}
