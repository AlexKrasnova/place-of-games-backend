package ru.geekbrains.traineeship.placeofgamesbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.ErrorDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.ErrorType;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.EventIsFullException;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.EventNotFoundException;

@ControllerAdvice
public class CommonExceptionsHandler {

    @ExceptionHandler(Throwable.class)

    public ResponseEntity<ErrorDTO> handleException(Exception exception, WebRequest request) {

        if (exception instanceof EventNotFoundException)
            return process(exception, ErrorType.EVENT_NOT_FOUND);

        if (exception instanceof EventIsFullException)
            return process(exception, ErrorType.EVENT_IS_FULL);

        if (exception instanceof HttpMessageNotReadableException)
            return process(exception, ErrorType.INVALID_REQUEST_PARAMS);

        if (exception instanceof MethodArgumentTypeMismatchException)
            return process(exception, ErrorType.INVALID_REQUEST_PARAMS);

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
