package de.iubh.fernstudium.ticketsystem.db.services;

import de.iubh.fernstudium.ticketsystem.db.entities.CommentEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;

import java.util.List;

public interface TicketDBService {

    /**
     * Ermittelt zu der übergebenen ID das Ticket
     *
     * @param id
     * @return TicketEntity
     */
    TicketEntity getTicketById(Long id);

    /**
     * Ermittelt zu einer UserID alle offenen Tickets
     *
     * @param user
     * @return Liste {@link TicketEntity}
     */
    List<TicketEntity> getOpenTicketsForUserId(UserEntity user);

    /**
     * Ändert den Status eines Tickets
     *
     * @param ticketId
     * @param newStatus
     */
    boolean changeStauts(Long ticketId, TicketStatus newStatus);

    /**
     * Fügt einen Kommentar zu einem Ticket hinzu
     *
     * @param ticketId
     * @param comment
     */
    TicketEntity addComment(Long ticketId, CommentEntity comment);

    /**
     * Liefert alle Tickets, die einem bestimmten User zugeordnet sind
     *
     * @param user
     * @return Liste {@link TicketEntity}
     */
    List<TicketEntity> getTicketsForUserId(UserEntity user);

    /**
     * Liefert alle Tickets, die von einem bestimmten User eingemeldet wurden
     *
     * @param user
     * @return Liste {@link TicketEntity}
     */
    List<TicketEntity> getTicketsReportedByUserId(UserEntity user);

    /**
     * Speichert ein neues Ticket in der Datenbank
     *
     * @param ticketEntity
     * @return TicketEntity
     */
    TicketEntity createTicket(TicketEntity ticketEntity);

    /**
     * Sucht Tickets mit angegebener oder ähnlicher Reporter-UserId
     *
     * @param reporter
     * @return Liste {@link TicketEntity}
     */
    List<TicketEntity> searchByReporter(String reporter);

    /**
     * Sucht Tickets mit angegebener oder ähnlicher Assignee-UserId
     *
     * @param assignee
     * @return Liste {@link TicketEntity}
     */
    List<TicketEntity> searchByAssignee(String assignee);

    /**
     * Sucht Tickets mit angegebenem oder ähnlichem Ticket-Status
     *
     * @param status
     * @return Liste {@link TicketEntity}
     */
    List<TicketEntity> searchByStatus(TicketStatus status);

    /**
     * Sucht Tickets mit angegebenem oder ähnlichem Title bzw. Beschreibung
     *
     * @param searchText
     * @return Liste {@link TicketEntity}
     */
    List<TicketEntity> searchByTitle(String searchText);

    /**
     * Sucht Tickets nach angegeben Stichwörter
     *
     * @param searchtext
     * @return Liste {@link TicketEntity}
     */
    List<TicketEntity> searchByDescription(String searchtext);

    /**
     * Führt eine bereitgestellte SQL-Query durch
     *
     * @param query
     * @param params
     * @return Liste {@link TicketEntity}
     */
    List<TicketEntity> searchByCustomQuery(String query, List<Object> params);

    /**
     * Erzeugt ein Master-Ticket samt untergeorneten Tickets
     *
     * @param masterTicketId
     * @param childTickets
     */
    TicketEntity createMasterTicket(Long masterTicketId, List<Long> childTickets) throws NoSuchTicketException;

    /**
     * Erzeugt ein Master-Ticket zu einem Kind-Ticket bzw. fügt ein Kind-Ticket einem bestehenden Master-Ticket hinzu
     *
     * @param masterTicketId
     * @param childTicketId
     */
    TicketEntity createMasterTicket(Long masterTicketId, Long childTicketId) throws NoSuchTicketException;

}
