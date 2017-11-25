package de.iubh.fernstudium.ticketsystem.services;

import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;

import java.util.List;
import java.util.concurrent.Future;

public interface SearchService {

    /**
     * Sucht nach TicketID
     *
     * @param id
     * @return Future von TicketDTO
     */
    Future<TicketDTO> searchById(Long id);

    /**
     * Sucht nach Einmeldern eines Tickets
     *
     * @param reporter
     * @return Future von Liste von TicketDTOs
     */
    Future<List<TicketDTO>> searchByReporter(String reporter);

    /**
     * Sucht nach Tickets, die einer Person zugewiesen sind
     * @param assignee
     * @return Future von Liste von TicketDTOs
     */
    Future<List<TicketDTO>> searchByAssignee(String assignee);

    /**
     * Sucht Tickets nach Status
     * @param ticketStatus
     * @return Future von Liste von TicketDTOs
     */
    Future<List<TicketDTO>> searchByStatus(TicketStatus ticketStatus);

    /**
     * Sucht Tickets nach Titel
     * @param text
     * @return Future von Liste von TicketDTOs
     */
    Future<List<TicketDTO>> searchByTitle(String text);

    /**
     * Sucht Tickets nach Beschreibung
     * @param searchtext
     * @return Future von Liste von TicketDTOs
     */
    Future<List<TicketDTO>> searchByDescription(String searchtext);

    /**
     * Suchet Tickets mit einem nativen SQL-Query
     * @param query
     * @param params
     * @return Future von Liste von TicketDTOs
     */
    Future<List<TicketDTO>> searchByQuery(String query, List<Object> params);

}
