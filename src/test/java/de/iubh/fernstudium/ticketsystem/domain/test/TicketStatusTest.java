package de.iubh.fernstudium.ticketsystem.domain.test;

import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import org.junit.Test;

import javax.xml.transform.sax.SAXSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        assertEquals("abgeschlossen", TicketStatus.CLO.getResolvedText());
        assertEquals("gelöscht", TicketStatus.DEL.getResolvedText());
        assertEquals("automatisch durch System gelöscht", TicketStatus.DLS.getResolvedText());
        assertEquals("Lösungsvorschlag IUBH", TicketStatus.RET.getResolvedText());
        assertEquals("abgebrochen durch IUBH", TicketStatus.UTU.getResolvedText());
    }

    @Test
    public void testFromString(){
        assertTrue(TicketStatus.fromString("neu") == TicketStatus.NEW);
        assertTrue(TicketStatus.fromString("in Bearbeitung durch IUBH") == TicketStatus.IPU);
        assertTrue(TicketStatus.fromString("in Bearbeitung durch Student") == TicketStatus.IPS);
        assertTrue(TicketStatus.fromString("gelöst") == TicketStatus.RES);
        assertTrue(TicketStatus.fromString("abgeschlossen") == TicketStatus.CLO);
        assertTrue(TicketStatus.fromString("gelöscht") == TicketStatus.DEL);
        assertTrue(TicketStatus.fromString("automatisch durch System gelöscht") == TicketStatus.DLS);
        assertTrue(TicketStatus.fromString("Lösungsvorschlag IUBH") == TicketStatus.RET);
        assertTrue(TicketStatus.fromString("abgebrochen durch IUBH") == TicketStatus.UTU);
        assertNull(TicketStatus.fromString("existiertNicht"));
    }
}
