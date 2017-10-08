package de.iubh.fernstudium.ticketsystem.services;

import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;

import java.util.List;
import java.util.concurrent.Future;

public interface SearchService {

    public Future<TicketDTO> searchById(Long id);

    public Future<List<TicketDTO>> searchByReporter(String reporter);

    public Future<List<TicketDTO>> searchByAssignee(String assignee);

    public Future<List<TicketDTO>> searchByStatus(TicketStatus ticketStatus);

    public Future<List<TicketDTO>> searchByTitle(String text);

    public Future<List<TicketDTO>> searchByDescription(String searchtext);
}
