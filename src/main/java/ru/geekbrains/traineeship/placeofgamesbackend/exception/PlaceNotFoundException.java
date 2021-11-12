package ru.geekbrains.traineeship.placeofgamesbackend.exception;

public class PlaceNotFoundException extends RuntimeException {

    public PlaceNotFoundException() {
        super("Place not found");
    }
}
