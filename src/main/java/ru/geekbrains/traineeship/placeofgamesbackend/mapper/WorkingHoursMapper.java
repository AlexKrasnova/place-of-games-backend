package ru.geekbrains.traineeship.placeofgamesbackend.mapper;

import org.springframework.stereotype.Component;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.WorkingHoursDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.WorkingHours;

@Component
public class WorkingHoursMapper {

    public WorkingHoursDTO mapToWorkingHoursDTO(WorkingHours workingHours) {
        return WorkingHoursDTO.builder()
                .dayOfWeek(workingHours.getDayOfWeek())
                .date(workingHours.getDate())
                .startTime(workingHours.getStartTime())
                .endTime(workingHours.getEndTime())
                .build();
    }
}
