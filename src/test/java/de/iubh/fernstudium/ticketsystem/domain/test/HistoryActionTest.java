package de.iubh.fernstudium.ticketsystem.domain.test;

import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HistoryActionTest {

    @Test
    public void testGetResolvedText(){
        assertEquals("Statusänderung", HistoryAction.SC.getResolvedText());
        assertEquals("Bearbeiter geändert", HistoryAction.AC.getResolvedText());
        assertEquals("Ticket erstellt", HistoryAction.CR.getResolvedText());
        assertEquals("Ticket abgeschlossen", HistoryAction.CL.getResolvedText());
        assertEquals("Kommentar hinzugefügt", HistoryAction.CA.getResolvedText());
        assertEquals("Abbruch durch Einmelder", HistoryAction.UC.getResolvedText());
    }

    @Test
    public void testFromString(){
        assertEquals(HistoryAction.SC, HistoryAction.fromString("Statusänderung"));
        assertEquals(HistoryAction.AC, HistoryAction.fromString("Bearbeiter geändert"));
        assertEquals(HistoryAction.CR, HistoryAction.fromString("Ticket erstellt"));
        assertEquals(HistoryAction.CL, HistoryAction.fromString("Ticket abgeschlossen"));
        assertEquals(HistoryAction.CA, HistoryAction.fromString("Kommentar hinzugefügt"));
        assertEquals(HistoryAction.UC, HistoryAction.fromString("Abbruch durch Einmelder"));
        assertNull(HistoryAction.fromString("notExists"));

    }
}
