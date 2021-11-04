package ru.geekbrains.traineeship.placeofgamesbackend.dto.error;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ErrorDTO {

    private final String message;

    private final String technicalDescription;

    private final ErrorType errorType;

    private final Instant timestamp = Instant.now();

}
