package de.iubh.fernstudium.ticketsystem.dtos.test;

import de.iubh.fernstudium.ticketsystem.db.entities.CommentEntity;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.dtos.CommentDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class CommentDTOTest {

    @Test
    public void testGetSet(){
        CommentDTO commentDTO = new CommentDTO();
        LocalDateTime ldt = LocalDateTime.now();
        UserDTO userDTO = buildUserDTO();
        commentDTO.setChangeDate(ldt);
        commentDTO.setAuthor(userDTO);
        commentDTO.setComment("Comment");
        commentDTO.setCreationDate(ldt);
        commentDTO.setId(1L);

        assertEquals(userDTO, commentDTO.getAuthor());
        assertEquals(ldt, commentDTO.getChangeDate());
        assertEquals(ldt, commentDTO.getCreationDate());
        assertEquals(new Long(1), commentDTO.getId());
        assertEquals("Comment", commentDTO.getComment());

        assertFalse(commentDTO.hashCode() == 0);
        assertNotNull(commentDTO.toString());
    }

    @Test
    public void testDTOtoEntity() throws InterruptedException {
        LocalDateTime ldt = LocalDateTime.now();
        CommentDTO commentDTO = new CommentDTO(ldt, buildUserDTO(), "comment", ldt);
        CommentDTO commentDTO1 = new CommentDTO(ldt, buildUserDTO(), "comment", ldt);
        CommentEntity commentEntity = commentDTO.toEntity();
        assertNotNull(commentEntity);
        assertEquals(commentDTO, commentDTO1);
        Thread.sleep(10);
        commentDTO.setChangeDate(LocalDateTime.now());
        assertNotEquals(commentDTO, commentDTO1);
        assertFalse(commentDTO.equals(commentDTO1));
        assertFalse(commentDTO.equals(null));
        assertTrue(commentDTO.equals(commentDTO));
    }

    private UserDTO buildUserDTO() {
        return new UserDTO("userid", "firstName", "lastName", "pw", UserRole.TU);
    }
}
