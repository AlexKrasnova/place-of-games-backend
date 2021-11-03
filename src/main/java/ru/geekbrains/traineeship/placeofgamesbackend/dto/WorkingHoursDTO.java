package ru.geekbrains.traineeship.placeofgamesbackend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class WorkingHoursDTO {

    private DayOfWeek dayOfWeek;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

}
