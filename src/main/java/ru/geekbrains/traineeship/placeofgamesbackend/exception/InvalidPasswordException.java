package ru.geekbrains.traineeship.placeofgamesbackend.exception;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super("Invalid password");
    }

}
