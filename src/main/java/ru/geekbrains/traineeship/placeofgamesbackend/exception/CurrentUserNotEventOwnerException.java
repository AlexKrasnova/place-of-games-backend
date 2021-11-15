package ru.geekbrains.traineeship.placeofgamesbackend.exception;

public class CurrentUserNotEventOwnerException extends RuntimeException {

    public CurrentUserNotEventOwnerException() { super ("Current user is not event owner"); }
}
