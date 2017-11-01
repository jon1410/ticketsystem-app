package de.iubh.fernstudium.ticketsystem.services;

import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Future;

public interface SearchService {

    Future<TicketDTO> searchById(Long id);

    Future<List<TicketDTO>> searchByReporter(String reporter);

    Future<List<TicketDTO>> searchByAssignee(String assignee);

    Future<List<TicketDTO>> searchByStatus(TicketStatus ticketStatus);

    Future<List<TicketDTO>> searchByTitle(String text);

    Future<List<TicketDTO>> searchByDescription(String searchtext);

    Future<List<TicketDTO>> searchByQuery(String query, List<Object> params);

}
