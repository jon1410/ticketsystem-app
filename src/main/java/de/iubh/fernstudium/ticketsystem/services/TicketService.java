package de.iubh.fernstudium.ticketsystem.services;

import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CommentDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;

import java.util.List;

/**
 * Created by ivanj on 03.07.2017.
 */
public interface TicketService {

    /**
     * Ermittelt ein bestimmtes Ticket mittels ID
     *
     * @param ticketId
     * @return TicketDTO
     * @throws NoSuchTicketException
     */
    TicketDTO getTicketByID(Long ticketId) throws NoSuchTicketException;

    /**
     * Ermittelt alle offenen Tickets zu einem User
     *
     * @param userId
     * @return java.util.List von {@link TicketDTO}
     * @throws UserNotExistsException
     */
    List<TicketDTO> getOpenTicketsForUserId(String userId) throws UserNotExistsException;

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
     */
    void changeStatus(Long ticketId, TicketStatus newStatus) throws NoSuchTicketException;

    /**
     * Fügt einen Kommentar zum Ticket hinzu
     *
     * @param ticketId
     * @param comment
     * @param userId
     * @throws NoSuchTicketException
     */
    void addComment(long ticketId, String comment, String userId) throws NoSuchTicketException, UserNotExistsException;

    /**
     * Erzeugt ein neues Ticket im System
     *
     * @param ticketDTO
     * @return TicketDTO incl. ID des Tickets
     */
    TicketDTO createTicket(TicketDTO ticketDTO);

}
