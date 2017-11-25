package de.iubh.fernstudium.ticketsystem.dtos.test;

import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserDTOTest {

    @Test
    public void testDTO(){

        UserDTO userDTO = new UserDTO("id", "firstName", "lastName", "pw", UserRole.TU);
        assertEquals("Tutor", userDTO.getRole());
        userDTO.setRole("Administrator");
        assertEquals("Administrator", userDTO.getRole());

        UserDTO userDTO1 = new UserDTO("id", "firstName", "lastName", "pw", UserRole.AD);
        assertEquals(userDTO, userDTO1);

        UserEntity userEntity = userDTO.toEntity();
        assertNotNull(userDTO);
        assertEquals("id", userEntity.getUserId());

        assertFalse(userDTO.hashCode() == 0);
        assertTrue(userDTO.equals(userDTO1));
        userDTO1.setLastName("newLastName");
        assertFalse(userDTO.equals(userDTO1));
        assertFalse(userDTO.equals(null));
        assertTrue(userDTO.equals(userDTO));
    }
}
