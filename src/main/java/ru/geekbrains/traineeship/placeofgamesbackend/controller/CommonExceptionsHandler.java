package ru.geekbrains.traineeship.placeofgamesbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.error.ErrorDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.error.ErrorType;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.*;

@Slf4j
@ControllerAdvice
public class CommonExceptionsHandler {

    @ExceptionHandler(Throwable.class)

    public ResponseEntity<ErrorDTO> handleException(Exception exception, WebRequest request) {

        log.error("Error has occurred", exception);

        if (exception instanceof EventNotFoundException)
            return process(exception, ErrorType.EVENT_NOT_FOUND);

        if (exception instanceof EventIsFullException)
            return process(exception, ErrorType.EVENT_IS_FULL);

        if (exception instanceof InvalidPasswordException)
            return process(exception, ErrorType.INVALID_PASSWORD);

        if (exception instanceof PlaceNotFoundException)
            return process(exception, ErrorType.PLACE_NOT_FOUND);

        if (exception instanceof UserAlreadyExistsException)
            return process(exception, ErrorType.USER_ALREADY_EXISTS);

        if (exception instanceof UserNotFoundException)
            return process(exception, ErrorType.USER_NOT_FOUND);

        if (exception instanceof HttpMessageNotReadableException)
            return process(exception, ErrorType.INVALID_REQUEST_PARAMS);

        if (exception instanceof MethodArgumentTypeMismatchException)
            return process(exception, ErrorType.INVALID_REQUEST_PARAMS);

        if (exception instanceof CurrentUserNotEnrolledException)
            return process(exception, ErrorType.CURRENT_USER_NOT_ENROLLED);

        if (exception instanceof UserAlreadyEnrolledException)
            return process(exception, ErrorType.USER_ALREADY_ENROLLED);

        if (exception instanceof AccessDeniedException)
            return process(exception, ErrorType.USER_UNAUTHORIZED);

        if (exception instanceof NotWorkingOrNotFreeTimePeriodException)
            return process(exception, ErrorType.NOT_WORKING_OR_NOT_FREE_TIME_PERIOD);

        if (exception instanceof CurrentUserNotEventOwnerException)
            return process(exception, ErrorType.USER_NOT_EVENT_OWNER);

        return process(exception, ErrorType.UNEXPECTED_ERROR);
    }

    private ResponseEntity<ErrorDTO> process(Exception exception, ErrorType errorType) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .technicalDescription(exception.getMessage())
                .message(errorType.getDescription())
                .errorType(errorType).build();
        return ResponseEntity.status(errorType.getHttpStatus()).body(errorDTO);
    }
}
