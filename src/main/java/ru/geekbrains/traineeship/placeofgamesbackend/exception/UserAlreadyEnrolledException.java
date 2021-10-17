package ru.geekbrains.traineeship.placeofgamesbackend.exception;

public class UserAlreadyEnrolledException extends RuntimeException{

    public UserAlreadyEnrolledException() {
        super("User already enrolled");
    }
}
