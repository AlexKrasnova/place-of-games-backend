package ru.geekbrains.traineeship.placeofgamesbackend.service;

import io.jsonwebtoken.lang.Collections;
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
import java.util.Optional;
import java.util.stream.Collectors;

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

    public boolean isTimeFree(Long placeId, TimePeriod timePeriod) {

        List<TimePeriod> freeTimes = getFreeTime(placeId, timePeriod.getStartTime().toLocalDate());
        freeTimes.addAll(getFreeTime(placeId, timePeriod.getStartTime().toLocalDate().minusDays(1)));

        return freeTimes.stream()
                .anyMatch(it -> (
                        it.getStartTime().compareTo(timePeriod.getStartTime()) <= 0 && it.getEndTime().compareTo(timePeriod.getEndTime()) >= 0
                ));
    }

    public List<TimePeriod> getFreeTime(Long placeId, LocalDate date) {

        List<WorkingHours> workingHoursList;

        try {
            workingHoursList = getPlaceWorkingHoursByDate(placeId, date);
        } catch (PlaceNotWorkingException e) {
            return List.of();
        }

        List<TimePeriod> result = new ArrayList<>();
        //todo: Расобраться с тем, что сейчас количество запросов в бд соответствует количеству отрезков рабочего времени.
        // Подумать о том, как можно сократить.
        workingHoursList.forEach(workingHours -> result.addAll(getFreeTime(workingHours, date)));

        return result;

    }

    private List<TimePeriod> getFreeTime(WorkingHours workingHours, LocalDate date) {

        LocalDateTime startTime = date.atTime(workingHours.getStartTime());
        LocalDateTime endTime = date.atTime(workingHours.getEndTime());

        if (workingHours.getStartTime().compareTo(workingHours.getEndTime()) > 0) {
            endTime = endTime.plusDays(1);
        }

        List<TimePeriod> eventTimes = getEventsByPlaceAndTimePeriod(workingHours.getPlaceId(), startTime, endTime);

        return buildFreeTimePeriods(startTime, endTime, eventTimes);
    }

    private List<TimePeriod> buildFreeTimePeriods(LocalDateTime startTime, LocalDateTime endTime, List<TimePeriod> busyPeriods) {

        LocalDateTime previousBusyPeriodEndTime = startTime;

        List<TimePeriod> freeTimePeriods = new ArrayList<>();

        for (TimePeriod busyTimePeriod : busyPeriods) {
            addNotEmptyTimePeriod(freeTimePeriods, previousBusyPeriodEndTime, busyTimePeriod.getStartTime());
            previousBusyPeriodEndTime = busyTimePeriod.getEndTime();
        }

        addNotEmptyTimePeriod(freeTimePeriods, previousBusyPeriodEndTime, endTime);

        return freeTimePeriods;
    }

    private void addNotEmptyTimePeriod(List<TimePeriod> timePeriods, LocalDateTime startTime, LocalDateTime endTime) {

        if (startTime.compareTo(endTime) < 0) {
            timePeriods.add(TimePeriod.builder()
                    .startTime(startTime)
                    .endTime(endTime)
                    .build());
        }
    }

    private List<TimePeriod> getEventsByPlaceAndTimePeriod(Long placeId, LocalDateTime startTime, LocalDateTime endTime) {

        List<Event> events = eventRepository.getEventsByPlaceAndTimePeriod(placeId, startTime, endTime);

        return events.stream().map(event -> TimePeriod.builder()
                        .startTime(event.getTime())
                        .endTime(event.getTime().plusMinutes(event.getDuration()))
                        .build())
                .collect(Collectors.toList());
    }

    private List<WorkingHours> getPlaceWorkingHoursByDate(Long placeId, LocalDate date) {

        Place place = placeRepository.getById(placeId);
        List<WorkingHours> workingHoursList = place.getWorkingHoursList();

        return findByDate(workingHoursList, date).orElse(
                findByDayOfWeek(workingHoursList, date.getDayOfWeek())
                        .orElseThrow(PlaceNotWorkingException::new)
        );

    }

    private Optional<List<WorkingHours>> findByDate(List<WorkingHours> workingHoursList, LocalDate date) {

        List<WorkingHours> result = workingHoursList.stream()
                .filter(it -> date.equals(it.getDate()))
                .collect(Collectors.toList());

        return optionalOf(result);

    }

    private Optional<List<WorkingHours>> findByDayOfWeek(List<WorkingHours> workingHoursList, DayOfWeek dayOfWeek) {

        List<WorkingHours> result = workingHoursList.stream()
                .filter(it -> dayOfWeek == it.getDayOfWeek())
                .collect(Collectors.toList());

        return optionalOf(result);

    }

    private Optional<List<WorkingHours>> optionalOf(List<WorkingHours> workingHoursList) {
        return Collections.isEmpty(workingHoursList) ? Optional.empty() : Optional.of(workingHoursList);
    }

}
