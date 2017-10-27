package de.iubh.fernstudium.ticketsystem.domain.test;

import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import org.junit.Test;

import javax.xml.transform.sax.SAXSource;

import static org.junit.Assert.assertEquals;

/**
 * Created by ivanj on 03.07.2017.
 */
public class TicketStatusTest {

    @Test
    public void testEnumTicketStatus(){
        assertEquals("neu", TicketStatus.NEW.getResolvedText());
        assertEquals("in Bearbeitung durch IUBH", TicketStatus.IPU.getResolvedText());
        assertEquals("in Bearbeitung durch Student", TicketStatus.IPS.getResolvedText());
        assertEquals("gelöst", TicketStatus.RES.getResolvedText());
        assertEquals("geschlossen", TicketStatus.CLO.getResolvedText());
        assertEquals("gelöscht", TicketStatus.DEL.getResolvedText());
        assertEquals("automatisch durch System gelöscht", TicketStatus.DLS.getResolvedText());
    }
}
