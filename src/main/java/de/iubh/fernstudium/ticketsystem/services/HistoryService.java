package de.iubh.fernstudium.ticketsystem.services;

import de.iubh.fernstudium.ticketsystem.dtos.HistoryDTO;

import java.util.List;

public interface HistoryService {

    /**
     * Ermittelt alle Änderungen zu einer TicketID.
     *
     * @param ticketId
     * @return List {@link HistoryDTO}
     */
    List<HistoryDTO> getHistoryForTicket(Long ticketId);

}
