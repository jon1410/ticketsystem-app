package de.iubh.fernstudium.ticketsystem.services;

import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import sun.security.krb5.internal.Ticket;

import java.util.List;

/**
 * Created by ivanj on 03.07.2017.
 */
public interface TicketService {

    /**
     * Ermittelt alle offenen Tickets zu einem User
     *
     * @param userId
     * @return java.util.List von {@link TicketDTO}
     */
    List<TicketDTO> getOpenTicketsForUserId(String userId);

    /**
     * Ermittelt alle historischen Tickets zu einem User
     *
     * @param userId
     * @return java.util.List von {@link TicketDTO}
     */
    List<TicketDTO> getHistoricTicketsByUserId(String userId);

    /**
     * Ändert den Status eines Tickets
     *
     * @param ticketId
     * @param newStatus
     * @return boolean, wenn Statusänderung erfolgreich
     */
    boolean changeStatus(Long ticketId, String newStatus);

    /**
     * Fügt einen Kommentar zum Ticket hinzu
     *
     * @param ticketId
     * @param comment
     * @return boolean, wenn erfolgreich
     */
    boolean addComment(long ticketId, String comment);

    /**
     * Erzeugt ein neues Ticket im System
     *
     * @param ticketDTO
     * @return TicketDTO incl. ID des Tickets
     */
    TicketDTO createTicket(TicketDTO ticketDTO);

}
