package de.iubh.fernstudium.ticketsystem.dtos.test;

import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.services.TicketDBService;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.CommentDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TicketDTOTest {

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @ObjectUnderTest
    private TicketDTO ticketDTO = new TicketDTO();

    @Inject
    private TicketDBService ticketDBService;

    @Test
    public void testGetSet(){

        ticketDTO.setDescription("test");
        assertEquals("test", ticketDTO.getDescription());

        ticketDTO.setTitle("title");
        assertEquals("title", ticketDTO.getTitle());

        ticketDTO.setId(1L);
        assertEquals(new Long(1L), ticketDTO.getId());

        ticketDTO.setAssignee(buildUserDTO());
        assertNotNull(ticketDTO.getAssignee());

        ticketDTO.setReporter(buildUserDTO());
        assertNotNull(ticketDTO.getReporter());

        ticketDTO.setTicketStatus(TicketStatus.NEW);
        assertEquals(TicketStatus.NEW, ticketDTO.getTicketStatus());

        ticketDTO.setCategory(buildCategoryDTO());
        assertNotNull(ticketDTO.getCategory());

        ticketDTO.setChildTicketsIds(null);
        assertNull(ticketDTO.getChildTicketsIds());

        ticketDTO.setComments(null);
        assertNull(ticketDTO.getComments());

        ticketDTO.setCreationTime(LocalDateTime.now());
        assertNotNull(ticketDTO.getCreationTime());
        assertNotNull(ticketDTO.getCreationTimeAsString());

        ticketDTO.setMasterTicketId(1L);
        assertNotNull(ticketDTO.getMasterTicketId());

        when(ticketDBService.getTicketById(anyLong())).thenReturn(new TicketEntity());
        List<CommentDTO> commentDTOS = new ArrayList<>();
        commentDTOS.add(new CommentDTO(buildUserDTO(), "comment"));

        List<Long> children = new ArrayList<>();
        children.add(new Long(5L));
        children.add(new Long(6L));

        ticketDTO.setComments(commentDTOS);
        ticketDTO.setChildTicketsIds(children);
        assertNotNull(ticketDTO.toEntity());

        assertFalse(ticketDTO.hashCode() == 0);
        assertFalse(ticketDTO.equals(null));
        assertFalse(ticketDTO.equals(new TicketDTO(3L, "title", "desc",
                buildUserDTO(), LocalDateTime.now(), buildCategoryDTO(), buildUserDTO(), null)));
        assertTrue(ticketDTO.equals(ticketDTO));

    }

    private UserDTO buildUserDTO() {
        return new UserDTO("userid", "firstName", "lastName", "pw", UserRole.TU);
    }

    private CategoryDTO buildCategoryDTO() {
        return new CategoryDTO("Test", "Test",
                buildUserDTO());
    }

}
