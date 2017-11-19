package de.iubh.fernstudium.ticketsystem.domain.exception;

public class CategoryNotFoundException extends Exception {

    public CategoryNotFoundException() {
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CategoryNotFoundException(Throwable cause) {
        super(cause);
    }

}
