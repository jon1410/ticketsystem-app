package de.iubh.fernstudium.ticketsystem.db.services;

import de.iubh.fernstudium.ticketsystem.db.entities.HistoryEntity;

import java.util.List;

public interface HistoryDBService {

    /**
     * Erzeugt einen neuen Eintrag auf der Historien-Tabelle
     *
     * @param historyEntity
     */
    void createHistoryEntry(HistoryEntity historyEntity);

    /**
     * Liefert die Historie zu einem Ticket
     *
     * @param ticketId
     * @return Liste von @{@link HistoryEntity}
     */
    List<HistoryEntity> getHistoryForTicketId(Long ticketId);
}
