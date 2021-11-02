package ru.geekbrains.traineeship.placeofgamesbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select distinct e from Event e left join e.place left join e.participants")
        // todo: Разобраться, почему не работает такой запрос @Query("select e from Event e join fetch e.place join fetch e.place.workingHoursList")
    List<Event> findAllWithPlacesAndUsers();

    @Query("select e from Event e where e.placeId =:placeId and e.time between :time1 and :time2")
    List<Event> getEventsByPlaceAndTimePeriod(@Param("placeId") Long placeId, @Param("time1") LocalDateTime time1, @Param("time2") LocalDateTime time2);

}
