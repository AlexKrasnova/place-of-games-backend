package ru.geekbrains.traineeship.placeofgamesbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Modifying
    @Query("update Event e set e.numberOfParticipants = e.numberOfParticipants + 1 where e.id = :eventId and e.numberOfParticipants < e.maxNumberOfParticipants")
    Integer addParticipant(@Param("eventId") Long eventId);

}
