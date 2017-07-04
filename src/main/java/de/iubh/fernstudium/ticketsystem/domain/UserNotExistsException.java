package de.iubh.fernstudium.ticketsystem.domain;

/**
 * Created by ivanj on 04.07.2017.
 */
public class UserNotExistsException extends Exception {

    public UserNotExistsException() {
    }

    public UserNotExistsException(String message) {
        super(message);
    }

    public UserNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotExistsException(Throwable cause) {
        super(cause);
    }
}
