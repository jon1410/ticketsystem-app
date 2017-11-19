package de.iubh.fernstudium.ticketsystem.dtos.test;

import de.iubh.fernstudium.ticketsystem.db.entities.HistoryEntity;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.HistoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class HistoryDTOTest {

    private LocalDateTime creationTime = LocalDateTime.now();

    @Test
    public void testHistoryDTO(){

        LocalDateTime ldt = LocalDateTime.now();
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setAction(HistoryAction.AC);
        historyDTO.setDetails("Details");
        historyDTO.setEventTime(ldt);
        historyDTO.setId(1L);
        TicketDTO ticketDTO = buildTicketDTO();
        historyDTO.setTicketDTO(ticketDTO);
        UserDTO userDTO = buildUserDTO();
        historyDTO.setUserDTO(userDTO);

        assertEquals(HistoryAction.AC, historyDTO.getAction());
        assertEquals("Details", historyDTO.getDetails());
        assertEquals(ldt, historyDTO.getEventTime());
        assertEquals(new Long(1), historyDTO.getId());
        assertNotNull(historyDTO.getTicketDTO());
        assertNotNull(historyDTO.getUserDTO());

        HistoryEntity historyEntity = historyDTO.toEntity();
        assertNotNull(historyEntity);

        HistoryDTO historyDTO1 = new HistoryDTO(1L, buildTicketDTO(), ldt, HistoryAction.AC, "Details", buildUserDTO());

        assertTrue(historyDTO.equals(historyDTO));
        assertTrue(historyDTO.equals(historyDTO1));
        assertFalse(historyDTO.equals(null));
        assertFalse(historyDTO.hashCode() == 0);
    }

    private TicketDTO buildTicketDTO() {
        return new TicketDTO(1L, "title", "desc",
                TicketStatus.NEW, buildUserDTO(), creationTime, buildCategoryDTO(), buildUserDTO(),
                null, null, null);
    }

    private CategoryDTO buildCategoryDTO() {
        return new CategoryDTO("cid", "name", buildUserDTO());
    }

    private UserDTO buildUserDTO() {
        return new UserDTO("userid", "firstName", "lastName", "pw", UserRole.TU);
    }
}
