package ru.geekbrains.traineeship.placeofgamesbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {

}
