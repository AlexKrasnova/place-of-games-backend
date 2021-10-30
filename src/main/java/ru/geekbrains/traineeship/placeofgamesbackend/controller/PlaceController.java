package ru.geekbrains.traineeship.placeofgamesbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.PlaceDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.PlaceDetailsDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.mapper.PlaceMapper;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Place;
import ru.geekbrains.traineeship.placeofgamesbackend.service.PlaceService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/places")
@Secured("ROLE_USER")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceMapper placeMapper;

    @GetMapping
    public List<PlaceDTO> findAll() {
        List<Place> places = placeService.findAll();
        return places.stream()
                .map(placeMapper::mapToPlaceDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PlaceDetailsDTO findById(@PathVariable Long id) {
        Place place = placeService.findById(id);
        return placeMapper.mapToPlaceDetailsDTO(place);
    }

}
