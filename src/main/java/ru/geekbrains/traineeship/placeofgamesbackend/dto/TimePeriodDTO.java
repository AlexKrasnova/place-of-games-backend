package ru.geekbrains.traineeship.placeofgamesbackend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TimePeriodDTO {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
