package ru.geekbrains.traineeship.placeofgamesbackend.exception;

public class CurrentUserNotEnrolledException extends RuntimeException {

    public CurrentUserNotEnrolledException() {
        super("The current user is not registered for the event");
    }
}
