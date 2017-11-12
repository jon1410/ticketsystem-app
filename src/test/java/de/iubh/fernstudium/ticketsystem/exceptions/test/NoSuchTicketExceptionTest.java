package de.iubh.fernstudium.ticketsystem.exceptions.test;

import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class NoSuchTicketExceptionTest {

    @Test(expected = NoSuchTicketException.class)
    public void testWithDefaultConstructor() throws NoSuchTicketException {
        NoSuchTicketException noSuchTicketException = new NoSuchTicketException();
        assertNotNull(noSuchTicketException);
        throw noSuchTicketException;
    }

    @Test(expected = NoSuchTicketException.class)
    public void testWithString() throws NoSuchTicketException {
        NoSuchTicketException noSuchTicketException = new NoSuchTicketException("Test");
        assertNotNull(noSuchTicketException);
        throw noSuchTicketException;
    }

    @Test(expected = NoSuchTicketException.class)
    public void testWithThrowable() throws NoSuchTicketException {
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException("Test");
        NoSuchTicketException noSuchTicketException = new NoSuchTicketException(userAlreadyExistsException);
        assertNotNull(noSuchTicketException);
        throw noSuchTicketException;
    }

    @Test(expected = NoSuchTicketException.class)
    public void testWithStringAndThrowAble() throws NoSuchTicketException {
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException("Test");
        NoSuchTicketException noSuchTicketException = new NoSuchTicketException("Test", userAlreadyExistsException);
        assertNotNull(noSuchTicketException);
        throw noSuchTicketException;
    }
}
