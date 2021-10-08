package ru.geekbrains.traineeship.placeofgamesbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e join fetch e.place ")
    List<Event> findAllWithPlaces();

    @Modifying
    @Query("update Event e set e.numberOfParticipants = e.numberOfParticipants + 1 where e.id = :eventId and e.numberOfParticipants < e.maxNumberOfParticipants")
    Integer addParticipant(@Param("eventId") Long eventId);

}
