package de.iubh.fernstudium.ticketsystem.db.services;

import de.iubh.fernstudium.ticketsystem.db.entities.CommentEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;

import java.util.List;
import java.util.concurrent.Future;

public interface TicketDBService {

    /**
     * Ermittelt zu der übergebenen ID das Ticket
     *
     * @param id
     * @return TicketEntity
     */
    public TicketEntity getTicketById(Long id);

    /**
     * Ermittelt zu einer UserID alle offenen Tickets
     *
     * @param user
     * @return Liste {@link TicketEntity}
     */
    public List<TicketEntity> getOpenTicketsForUserId(UserEntity user);

    /**
     * Ändert den Status eines Tickets
     *
     * @param ticketId
     * @param newStatus
     */
    public boolean changeStauts(Long ticketId, TicketStatus newStatus);

    /**
     * Fügt einen Kommentar zu einem Ticket hinzu
     *
     * @param ticketId
     * @param comment
     */
    public boolean addComment(Long ticketId, CommentEntity comment);

    /**
     * Speichert ein neues Ticket in der Datenbank
     *
     * @param ticketEntity
     * @return TicketEntity
     */
    public TicketEntity createTicket(TicketEntity ticketEntity);

    /**
     * Sucht Tickets mit angegebener oder ähnlicher Reporter-UserId
     *
     * @param reporter
     * @return Liste {@link TicketEntity}
     */
    public List<TicketEntity> searchByReporter(UserEntity reporter);

    /**
     * Sucht Tickets mit angegebener oder ähnlicher Assignee-UserId
     *
     * @param assignee
     * @return Liste {@link TicketEntity}
     */
    public List<TicketEntity> searchByAssignee(UserEntity assignee);

    /**
     * Sucht Tickets mit angegebenem oder ähnlichem Ticket-Status
     *
     * @param status
     * @return Liste {@link TicketEntity}
     */
    public List<TicketEntity> searchByStatus(TicketStatus status);

    /**
     * Sucht Tickets mit angegebenem oder ähnlichem Title bzw. Beschreibung
     *
     * @param searchText
     * @return Liste {@link TicketEntity}
     */
    public List<TicketEntity> searchByTitle(String searchText);

    /**
     * Sucht Tickets nach angegeben Stichwörter
     *
     * @param searchtext
     * @return Liste {@link TicketEntity}
     */
    public List<TicketEntity> searchByDescription(String searchtext);
}
