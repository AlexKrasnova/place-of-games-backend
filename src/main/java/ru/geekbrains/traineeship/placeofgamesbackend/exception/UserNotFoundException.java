package ru.geekbrains.traineeship.placeofgamesbackend.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() { super("User not found"); }
}
