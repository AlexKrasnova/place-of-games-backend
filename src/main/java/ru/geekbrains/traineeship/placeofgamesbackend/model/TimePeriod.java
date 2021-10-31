package ru.geekbrains.traineeship.placeofgamesbackend.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
public class TimePeriod {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
