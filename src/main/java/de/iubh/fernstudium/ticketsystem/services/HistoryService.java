package de.iubh.fernstudium.ticketsystem.services;

import de.iubh.fernstudium.ticketsystem.dtos.TicketHistoryDTO;

import java.util.List;

public interface HistoryService {

    /**
     * Ermittelt alle Änderungen zu einer TicketID.
     *
     * @param ticketId
     * @return List {@link TicketHistoryDTO}
     */
    public List<TicketHistoryDTO> getHistoryForTicket(Long ticketId);

}
