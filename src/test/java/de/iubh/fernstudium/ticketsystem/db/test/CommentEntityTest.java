package de.iubh.fernstudium.ticketsystem.db.test;

import de.iubh.fernstudium.ticketsystem.db.entities.CommentEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CommentEntityTest {

    @Test
    public void testGetSet(){

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(1L);
        assertEquals(new Long(1L), commentEntity.getId());

        commentEntity.setAuthor(buildUserEntity());
        assertNotNull(commentEntity.getAuthor());

        commentEntity.setChangeDate(DateTimeUtil.now());
        assertNotNull(commentEntity.getChangeDate());

        commentEntity.setComment("Test");
        assertEquals("Test", commentEntity.getComment());

        commentEntity.setCreationDate(DateTimeUtil.now());
        assertNotNull(commentEntity.getCreationDate());

        CommentEntity commentEntity1 = new CommentEntity(DateTimeUtil.now(), buildUserEntity(), "Test", DateTimeUtil.now());
        assertNotNull(commentEntity1.toDto());

        commentEntity1 = new CommentEntity(LocalDateTime.now(), buildUserEntity(), "Test", LocalDateTime.now());
        assertNotNull(commentEntity1.toDto());
    }

    private UserEntity buildUserEntity() {
        return new UserEntity("userid", "first", "last", "pw", UserRole.TU);
    }
}
