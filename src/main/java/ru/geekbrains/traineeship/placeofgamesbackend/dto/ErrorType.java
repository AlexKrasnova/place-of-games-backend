package ru.geekbrains.traineeship.placeofgamesbackend.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@Getter
public enum ErrorType {

    EVENT_NOT_FOUND("Мероприятие не найдено", HttpStatus.NOT_FOUND),
    EVENT_IS_FULL("Свободных мест нет", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD("Неверный пароль", HttpStatus.BAD_REQUEST),
    USER_ALREADY_EXISTS("Пользователь уже существует", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("Пользователь не найден", HttpStatus.NOT_FOUND),
    PLACE_NOT_FOUND("Площадка не найдена", HttpStatus.NOT_FOUND),
    UNEXPECTED_ERROR("Неизвестная ошибка", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST_PARAMS("Неверные параметры запроса", HttpStatus.BAD_REQUEST),
    CURRENT_USER_NOT_ENROLLED("Текущий пользователь не зарегистрирован на мероприятии", HttpStatus.BAD_REQUEST),
    USER_ALREADY_ENROLLED("Текущий пользоавтель уже зарегистрирован на мероприятии", HttpStatus.BAD_REQUEST),
    USER_UNAUTHORIZED("Доступ закрыт,", HttpStatus.UNAUTHORIZED);

    private String description;
    private HttpStatus httpStatus;

    ErrorType(String description, HttpStatus httpStatus) {
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
