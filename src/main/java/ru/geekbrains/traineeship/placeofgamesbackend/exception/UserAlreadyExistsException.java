package ru.geekbrains.traineeship.placeofgamesbackend.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("User already exists");
    }
}
