package de.iubh.fernstudium.ticketsystem.services;

import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
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
     * @return java.util.List von  TicketDTOs
     * @throws UserNotExistsException
     */
    List<TicketDTO> getOpenTicketsForUserId(String userId) throws UserNotExistsException;

    /**
     * Liefert alle Tickets, die von einem Benutzer eingemeldet wurden
     *
     * @param userId
     * @return Liste von TicketDTO
     * @throws UserNotExistsException
     */
    List<TicketDTO> getTicketsReportedByUserId(String userId) throws UserNotExistsException;

    /**
     * Ändert den Status eines Tickets
     *
     * @param ticketId
     * @param newStatus
     * @throws NoSuchTicketException
     */
    void changeStatus(Long ticketId, TicketStatus newStatus) throws NoSuchTicketException;

    /**
     * Fügt einen Kommentar zum Ticket hinzu
     *
     * @param ticketId
     * @param comment
     * @param userId
     * @throws NoSuchTicketException
     * @throws UserNotExistsException
     */
    TicketDTO addComment(long ticketId, String comment, String userId) throws NoSuchTicketException, UserNotExistsException;

    /**
     * Erzeugt ein neues Ticket im System
     *
     * @param ticketDTO
     * @return TicketDTO incl. ID des Tickets
     */
    TicketDTO createTicket(TicketDTO ticketDTO);

    /**
     * Erzeugt ein Master-Ticket samt untergeorneten Tickets
     *
     * @param masterTicketId
     * @param childTickets
     * @throws NoSuchTicketException
     */
    TicketDTO createMasterTicket(Long masterTicketId, List<Long> childTickets) throws NoSuchTicketException;

    /**
     * Erzeugt ein Master-Ticket zu einem Kind-Ticket bzw. fügt ein Kind-Ticket einem bestehenden Master-Ticket hinzu
     *
     * @param masterTicketId
     * @param childTicketId
     * @throws NoSuchTicketException
     */
    TicketDTO createMasterTicket(Long masterTicketId, Long childTicketId) throws NoSuchTicketException;

}
