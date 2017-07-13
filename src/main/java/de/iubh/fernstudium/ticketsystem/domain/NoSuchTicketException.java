package de.iubh.fernstudium.ticketsystem.domain;

/**
 * Created by ivanj on 13.07.2017.
 */
public class NoSuchTicketException extends Exception {

    public NoSuchTicketException() {
    }

    public NoSuchTicketException(String message) {
        super(message);
    }

    public NoSuchTicketException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchTicketException(Throwable cause) {
        super(cause);
    }

    public NoSuchTicketException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
