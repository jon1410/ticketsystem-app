package de.iubh.fernstudium.ticketsystem.db.test;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.CommentEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TicketEntityTest {

    @Test
    public void testGetSet(){
        TicketEntity ticketEntity = new TicketEntity("Title", "desc",
                TicketStatus.NEW, buildUserEntity(), DateTimeUtil.now(), buildCategoryEntity(), buildUserEntity(),buildComments());

        ticketEntity.setId(1L);
        assertEquals(new Long(1), ticketEntity.getId());
        assertEquals("Title", ticketEntity.getTitle());
        assertEquals("desc", ticketEntity.getDescription());
        assertEquals(5, ticketEntity.getComments().size());
        assertNotNull(ticketEntity.getCategory());
        assertNotNull(ticketEntity.getCreationTime());
        assertNotNull(ticketEntity.getReporter());

        TicketDTO ticketDTO = ticketEntity.toDto();
        assertNotNull(ticketDTO);
    }

    private List<CommentEntity> buildComments() {
        List<CommentEntity> cList = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
             cList.add(buildComment(i));
        }
        return cList;
    }

    private CommentEntity buildComment(int i) {
        return new CommentEntity(DateTimeUtil.now(), buildUserEntity(), "Comment", DateTimeUtil.now());
    }

    private CategoryEntity buildCategoryEntity() {
        return new CategoryEntity("catID", "name", buildUserEntity());
    }

    private UserEntity buildUserEntity() {
        return new UserEntity("userid", "first", "last", "pw", UserRole.TU);
    }
}
