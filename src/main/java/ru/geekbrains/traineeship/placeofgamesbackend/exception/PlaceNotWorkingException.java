package ru.geekbrains.traineeship.placeofgamesbackend.exception;

public class PlaceNotWorkingException extends RuntimeException {

    public PlaceNotWorkingException() {
        super("Place does not work at the specified time");
    }
}
