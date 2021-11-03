package ru.geekbrains.traineeship.placeofgamesbackend.mapper;

import org.springframework.stereotype.Component;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.TimePeriodDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.TimePeriod;

@Component
public class TimePeriodMapper {

    public TimePeriodDTO mapToTimePeriodDTO(TimePeriod timePeriod) {
        return TimePeriodDTO.builder()
                .startTime(timePeriod.getStartTime())
                .endTime(timePeriod.getEndTime())
                .build();
    }
}
