package de.iubh.fernstudium.ticketsystem.db.test;

import de.iubh.fernstudium.ticketsystem.db.util.JPAHibernateTestManager;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.jws.soap.SOAPBinding;

import static org.junit.Assert.*;

/**
 * Created by ivanj on 16.07.2017.
 */
public class UserEntityTest {

    @Test
    public void testGetSet(){
        UserEntity userEntity = new UserEntity();
        userEntity.setRole(UserRole.TU);
        assertEquals(UserRole.TU, userEntity.getRole());

        userEntity.setPassword("pw");
        assertEquals("pw", userEntity.getPassword());

        userEntity.setFirstName("firstName");
        assertEquals("firstName", userEntity.getFirstName());

        userEntity.setLastName("lastName");
        assertEquals("lastName", userEntity.getLastName());

        userEntity.setUserId("id");
        assertEquals("id", userEntity.getUserId());

        String s = userEntity.toString();
        assertNotNull(s);

        UserDTO dto = userEntity.toDto();
        assertNotNull(dto);

        assertTrue(userEntity.equals(userEntity));
        assertFalse(userEntity.equals(null));

        UserEntity u1 = new UserEntity("xy", "first", "last", "pw", UserRole.TU);
        assertFalse(userEntity.equals(u1));

        u1 = new UserEntity("id", "firstName", "lastName", "pw", UserRole.TU);
        assertTrue(userEntity.equals(u1));

        assertFalse(userEntity.hashCode() == 0);
    }
}
