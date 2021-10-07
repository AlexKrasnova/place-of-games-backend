package ru.geekbrains.traineeship.placeofgamesbackend.exception;

public class EventIsFullException extends RuntimeException {

    public EventIsFullException() {
        super("На мероприятии нет свободных мест");
    }
}
