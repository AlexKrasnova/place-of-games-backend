package ru.geekbrains.traineeship.placeofgamesbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select distinct e from Event e left join e.place left join e.participants")
        // todo: Разобраться, почему не работает такой запрос @Query("select e from Event e join fetch e.place join fetch e.place.workingHoursList")
    List<Event> findAllWithPlacesAndUsers();
}
