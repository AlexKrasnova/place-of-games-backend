package ru.geekbrains.traineeship.placeofgamesbackend.exception;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException() {
        super("Мероприятие не найдено");
    }

}
