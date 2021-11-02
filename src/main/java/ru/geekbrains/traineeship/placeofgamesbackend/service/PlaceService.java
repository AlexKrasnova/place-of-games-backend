package ru.geekbrains.traineeship.placeofgamesbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.PlaceNotFoundException;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.PlaceNotWorkingException;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Place;
import ru.geekbrains.traineeship.placeofgamesbackend.model.TimePeriod;
import ru.geekbrains.traineeship.placeofgamesbackend.model.WorkingHours;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.EventRepository;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.PlaceRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final EventRepository eventRepository;

    public List<Place> findAll() {
        return placeRepository.findAllWithWorkingHours();
    }

    public Place findById(Long id) {
        return placeRepository.findById(id).orElseThrow(PlaceNotFoundException::new);
    }

    public List<TimePeriod> getFreeTime(Long id, LocalDate date) {

        List<TimePeriod> freeTimePeriods = new ArrayList<>();

        WorkingHours workingHours = getWorkingHoursByDate(id, date);

        LocalDateTime startTime;
        LocalDateTime endTime;

        if (workingHours.getStartTime().compareTo(workingHours.getEndTime()) < 0) {
            startTime = date.atTime(workingHours.getStartTime());
            endTime = date.atTime(workingHours.getEndTime());
        } else {
            startTime = date.atTime(workingHours.getStartTime());
            endTime = date.plusDays(1).atTime(workingHours.getEndTime());
        }

        List<TimePeriod> eventTimes = getEventTimesByDate(id, startTime, endTime);

        LocalDateTime startTimeTemp = startTime;
        LocalDateTime endTimeTemp = startTime;

        for (int i = 0; i < eventTimes.size(); i++) {
            if (i == 0 || !eventTimes.get(i - 1).getEndTime().equals(eventTimes.get(i).getStartTime())) {
                if (startTimeTemp.compareTo(eventTimes.get(i).getStartTime()) < 0) {
                    endTimeTemp = eventTimes.get(i).getStartTime();
                }

                freeTimePeriods.add(TimePeriod.builder()
                        .startTime(startTimeTemp)
                        .endTime(endTimeTemp)
                        .build());


            }
            startTimeTemp = eventTimes.get(i).getEndTime();
        }

        if (startTimeTemp.compareTo(endTime) < 0) {
            freeTimePeriods.add(TimePeriod.builder()
                    .startTime(startTimeTemp)
                    .endTime(endTime)
                    .build());
        }

        return freeTimePeriods;
    }

    private List<TimePeriod> getEventTimesByDate(Long id, LocalDateTime startTime, LocalDateTime endTime) {

        List<Event> events = eventRepository.getEventsByPlaceAndTimePeriod(id, startTime, endTime);

        List<TimePeriod> eventTimes = new ArrayList<>();
        for (Event event : events) {
            TimePeriod timePeriod = TimePeriod.builder()
                    .startTime(event.getTime())
                    .endTime(event.getTime().plusMinutes(event.getDuration()))
                    .build();
            eventTimes.add(timePeriod);
        }

        return eventTimes;
    }

    private WorkingHours getWorkingHoursByDate(Long id, LocalDate date) {
        Place place = placeRepository.getById(id);
        List<WorkingHours> workingHoursList = place.getWorkingHoursList();

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        WorkingHours workingHours = new WorkingHours();

        for (WorkingHours workingHours1 : workingHoursList) {
            if (workingHours1.getDate() != null && workingHours1.getDate().equals(date)) {
                workingHours = workingHours1;
            }

        }
        if (workingHours.getStartTime() == null) {
            for (WorkingHours workingHours1 : workingHoursList) {
                if (workingHours1.getDayOfWeek() != null && workingHours1.getDayOfWeek().equals(dayOfWeek)) {
                    workingHours = workingHours1;
                }
            }
        }

        if (workingHours.getStartTime() == null) {
            throw new PlaceNotWorkingException();
        }

        return workingHours;
    }
}
