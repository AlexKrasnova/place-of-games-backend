package ru.geekbrains.traineeship.placeofgamesbackend.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    EVENT_NOT_FOUND("Мероприятие не найдено", HttpStatus.NOT_FOUND),
    EVENT_IS_FULL("Свободных мест нет", HttpStatus.BAD_REQUEST),
    UNEXPECTED_ERROR("Неизвестная ошибка", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST_PARAMS("Неверные параметры запроса",HttpStatus.BAD_REQUEST);

    private String description;
    private HttpStatus httpStatus;

    ErrorType(String description, HttpStatus httpStatus) {
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
