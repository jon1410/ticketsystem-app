package de.iubh.fernstudium.ticketsystem.exceptions.test;

import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidCredentialsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class InvalidCredentialsExceptionTest {

    @Test(expected = InvalidCredentialsException.class)
    public void testWithDefaultConstructor() throws InvalidCredentialsException {
        InvalidCredentialsException invalidCredentialsException = new InvalidCredentialsException();
        assertNotNull(invalidCredentialsException);
        throw invalidCredentialsException;
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testWithString() throws InvalidCredentialsException {
        InvalidCredentialsException invalidCredentialsException = new InvalidCredentialsException("Test");
        assertNotNull(invalidCredentialsException);
        throw invalidCredentialsException;
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testWithThrowable() throws InvalidCredentialsException {
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException("Test");
        InvalidCredentialsException invalidCredentialsException = new InvalidCredentialsException(userAlreadyExistsException);
        assertNotNull(invalidCredentialsException);
        throw invalidCredentialsException;
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testWithStringAndThrowAble() throws InvalidCredentialsException {
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException("Test");
        InvalidCredentialsException invalidCredentialsException = new InvalidCredentialsException("Test", userAlreadyExistsException);
        assertNotNull(invalidCredentialsException);
        throw invalidCredentialsException;
    }
}
