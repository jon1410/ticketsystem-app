package de.iubh.fernstudium.ticketsystem.exceptions.test;

import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CategoryNotFoundExceptionTest {

    @Test(expected = CategoryNotFoundException.class)
    public void testWithDefaultConstructor() throws CategoryNotFoundException {
        CategoryNotFoundException categoryNotFoundException = new CategoryNotFoundException();
        assertNotNull(categoryNotFoundException);
        throw categoryNotFoundException;
    }

    @Test(expected = CategoryNotFoundException.class)
    public void testWithString() throws CategoryNotFoundException {
        CategoryNotFoundException categoryNotFoundException = new CategoryNotFoundException("Test");
        assertNotNull(categoryNotFoundException);
        throw categoryNotFoundException;
    }

    @Test(expected = CategoryNotFoundException.class)
    public void testWithThrowable() throws CategoryNotFoundException {
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException("Test");
        CategoryNotFoundException categoryNotFoundException = new CategoryNotFoundException(userAlreadyExistsException);
        assertNotNull(categoryNotFoundException);
        throw categoryNotFoundException;
    }

    @Test(expected = CategoryNotFoundException.class)
    public void testWithStringAndThrowAble() throws CategoryNotFoundException {
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException("Test");
        CategoryNotFoundException categoryNotFoundException = new CategoryNotFoundException("Test", userAlreadyExistsException);
        assertNotNull(categoryNotFoundException);
        throw categoryNotFoundException;
    }
}
