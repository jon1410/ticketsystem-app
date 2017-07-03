package de.iubh.fernstudium.ticketsystem.domain.test;

import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ivanj on 03.07.2017.
 */
public class TicketStatusTest {

    @Test
    public void testEnumTicketStatus(){
        assertEquals("neu", TicketStatus.NEW.getResolvedText());
        assertEquals("in Bearbeitung durch IUBH", TicketStatus.IN_PROGRESS_IUBH.getResolvedText());
        assertEquals("in Bearbeitung durch Student", TicketStatus.IN_PROGRESS_STUDENT.getResolvedText());
        assertEquals("gelöst", TicketStatus.RESOLVED.getResolvedText());
        assertEquals("geschlossen", TicketStatus.CLOSED.getResolvedText());
        assertEquals("gelöscht", TicketStatus.DELETED.getResolvedText());
        assertEquals("automatisch durch System gelöscht", TicketStatus.DELETED_SYSTEM.getResolvedText());
    }
}
