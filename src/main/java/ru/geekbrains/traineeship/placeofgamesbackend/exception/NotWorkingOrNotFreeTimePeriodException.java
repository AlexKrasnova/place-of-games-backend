package ru.geekbrains.traineeship.placeofgamesbackend.exception;

public class NotWorkingOrNotFreeTimePeriodException extends RuntimeException {

    public NotWorkingOrNotFreeTimePeriodException() {
        super("Place not working or time interval is not free");
    }
}
