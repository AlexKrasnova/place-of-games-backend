package ru.geekbrains.traineeship.placeofgamesbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.PlaceNotFoundException;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Place;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.PlaceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    public List<Place> findAll() {
        return placeRepository.findAll();
    }

    public Place findById(Long id) {
        return placeRepository.findById(id).orElseThrow(PlaceNotFoundException::new);
    }

}
