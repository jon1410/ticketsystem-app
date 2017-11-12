package de.iubh.fernstudium.ticketsystem.exceptions.test;

import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class UserAlreadyExistsExceptionTest {

    @Test(expected = UserAlreadyExistsException.class)
    public void testWithDefaultConstructor() throws UserAlreadyExistsException {
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException();
        assertNotNull(userAlreadyExistsException);
        throw userAlreadyExistsException;
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testWithString() throws UserAlreadyExistsException {
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException("Test");
        assertNotNull(userAlreadyExistsException);
        throw userAlreadyExistsException;
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testWithThrowable() throws UserAlreadyExistsException {
        NoSuchTicketException noSuchTicketException = new NoSuchTicketException("Test");
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException(noSuchTicketException);
        assertNotNull(userAlreadyExistsException);
        throw userAlreadyExistsException;
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testWithStringAndThrowAble() throws UserAlreadyExistsException {
        NoSuchTicketException noSuchTicketException = new NoSuchTicketException("Test");
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException("Test", noSuchTicketException);
        assertNotNull(userAlreadyExistsException);
        throw userAlreadyExistsException;
    }
}
