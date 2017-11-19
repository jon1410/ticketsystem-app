package de.iubh.fernstudium.ticketsystem.dtos.test;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.CommentEntity;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.CommentDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class CategoryDTOTest {

    @Test
    public void testDTOtoEntity(){
        CategoryDTO categoryDTO = new CategoryDTO("c1", "name", buildUserDTO());
        CategoryDTO categoryDTO1 = new CategoryDTO("c1", "name", buildUserDTO());
        CategoryEntity categoryEntity = categoryDTO.toEntity();
        assertNotNull(categoryEntity);
        assertEquals(categoryDTO, categoryDTO1);
        categoryDTO.setCategoryName("xy");
        assertNotEquals(categoryDTO, categoryDTO1);
        assertFalse(categoryDTO.hashCode() == 0);
    }

    private UserDTO buildUserDTO() {
        return new UserDTO("userid", "firstName", "lastName", "pw", UserRole.TU);
    }
}
