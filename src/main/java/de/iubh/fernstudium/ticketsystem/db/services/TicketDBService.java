package de.iubh.fernstudium.ticketsystem.db.services;

import de.iubh.fernstudium.ticketsystem.db.entities.CommentEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;

import java.util.List;

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
}