package de.iubh.fernstudium.ticketsystem.domain.test;

import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.event.payload.HistoryPayload;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HistoryPayloadTest {

    @Test
    public void testHistoryPayload(){
        HistoryPayload historyPayload = new HistoryPayload();

        LocalDateTime ldt = LocalDateTime.now();
        historyPayload.setDetails("Details");
        historyPayload.setTicketId(1L);
        historyPayload.setEventFired(ldt);
        historyPayload.setEventName("Test");
        historyPayload.setUserId(buildUserDTO());

        assertEquals("Details", historyPayload.getDetails());
        assertEquals(new Long(1), historyPayload.getTicketId());
        assertEquals(ldt, historyPayload.getEventFired());
        assertEquals("Test", historyPayload.getEventName());
        assertNotNull(historyPayload.getUserId());
    }

    private UserDTO buildUserDTO() {
        return new UserDTO("userid", "firstName", "lastName", "pw", UserRole.TU);
    }
}
