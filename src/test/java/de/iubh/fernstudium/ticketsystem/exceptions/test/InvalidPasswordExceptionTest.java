package de.iubh.fernstudium.ticketsystem.exceptions.test;

import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class InvalidPasswordExceptionTest {

    @Test(expected = InvalidPasswordException.class)
    public void testWithDefaultConstructor() throws InvalidPasswordException {
        InvalidPasswordException invalidPasswordException = new InvalidPasswordException();
        assertNotNull(invalidPasswordException);
        throw invalidPasswordException;
    }

    @Test(expected = InvalidPasswordException.class)
    public void testWithString() throws InvalidPasswordException {
        InvalidPasswordException invalidPasswordException = new InvalidPasswordException("Test");
        assertNotNull(invalidPasswordException);
        throw invalidPasswordException;
    }

    @Test(expected = InvalidPasswordException.class)
    public void testWithThrowable() throws InvalidPasswordException {
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException("Test");
        InvalidPasswordException invalidPasswordException = new InvalidPasswordException(userAlreadyExistsException);
        assertNotNull(invalidPasswordException);
        throw invalidPasswordException;
    }

    @Test(expected = InvalidPasswordException.class)
    public void testWithStringAndThrowAble() throws InvalidPasswordException {
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException("Test");
        InvalidPasswordException invalidPasswordException = new InvalidPasswordException("Test", userAlreadyExistsException);
        assertNotNull(invalidPasswordException);
        throw invalidPasswordException;
    }
}
