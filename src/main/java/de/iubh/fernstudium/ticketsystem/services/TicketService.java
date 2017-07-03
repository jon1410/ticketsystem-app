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
    public List<TicketDTO> getOpenTicketsForUserId(String userId);

    /**
     * Fügt einen Kommentar zum Ticket hinzu
     *
     * @param comment
     * @return boolean, wenn erfolgreich
     */
    public boolean addComment(String comment);

    /**
     * Erzeugt ein neues Ticket im System
     * @param ticketDTO
     * @return
     */
    public TicketDTO createTicket(TicketDTO ticketDTO);

}
