package de.iubh.fernstudium.ticketsystem.exceptions.test;

import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class UserNotExistsExceptionTest {

    @Test(expected = UserNotExistsException.class)
    public void testWithDefaultConstructor() throws UserNotExistsException {
        UserNotExistsException userNotExistsException = new UserNotExistsException();
        assertNotNull(userNotExistsException);
        throw userNotExistsException;
    }

    @Test(expected = UserNotExistsException.class)
    public void testWithString() throws UserNotExistsException {
        UserNotExistsException userNotExistsException = new UserNotExistsException("Test");
        assertNotNull(userNotExistsException);
        throw userNotExistsException;
    }

    @Test(expected = UserNotExistsException.class)
    public void testWithThrowable() throws UserNotExistsException {
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException("Test");
        UserNotExistsException userNotExistsException = new UserNotExistsException(userAlreadyExistsException);
        assertNotNull(userNotExistsException);
        throw userNotExistsException;
    }

    @Test(expected = UserNotExistsException.class)
    public void testWithStringAndThrowAble() throws UserNotExistsException {
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException("Test");
        UserNotExistsException userNotExistsException = new UserNotExistsException("Test", userAlreadyExistsException);
        assertNotNull(userNotExistsException);
        throw userNotExistsException;
    }
}
