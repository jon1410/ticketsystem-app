package de.iubh.fernstudium.ticketsystem.services.impl;

import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.services.TicketDBService;
import de.iubh.fernstudium.ticketsystem.db.services.UserDBService;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CommentDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import de.iubh.fernstudium.ticketsystem.services.UserService;

import javax.ejb.Asynchronous;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
public class TicketServiceImpl implements TicketService {

    @Inject
    private TicketDBService ticketDBService;

    @Inject
    private UserService userService;

    @Override
    public TicketDTO getTicketByID(Long ticketId) throws NoSuchTicketException {
        TicketEntity ticketEntity = ticketDBService.getTicketById(ticketId);
        if(ticketEntity == null){
            throw new NoSuchTicketException("Ticket mit ID: " + ticketId + " wurde nicht gefunden.");
        }
        return ticketEntity.toDto();
    }

    @Override
    public List<TicketDTO> getOpenTicketsForUserId(String userId) throws UserNotExistsException {

        UserDTO userDTO = userService.getUserByUserId(userId);
        List<TicketEntity> ticketEntityList = ticketDBService.getOpenTicketsForUserId(userDTO.toEntity());

        List<TicketDTO> ticketDTOList = new ArrayList<>(ticketEntityList.size());
        for(TicketEntity t : ticketEntityList){
            ticketDTOList.add(t.toDto());
        }
        return ticketDTOList;
    }

    @Override
    public List<TicketDTO> getHistoricTicketsByUserId(String userId) {
        //TODO evtl. in HistoryService verschieben
        return null;
    }

    @Override
    public void changeStatus(Long ticketId, TicketStatus newStatus) throws NoSuchTicketException {
        if(!ticketDBService.changeStauts(ticketId, newStatus)){
            throw new NoSuchTicketException("Ticket mit ID: " + ticketId + " wurde nicht gefunden.");
        }
    }

    @Override
    public TicketDTO addComment(long ticketId, String comment, String userId) throws NoSuchTicketException, UserNotExistsException {
        UserDTO user = userService.getUserByUserId(userId);
        CommentDTO commentDTO = new CommentDTO(user, comment);
        TicketEntity ticketEntity = ticketDBService.addComment(ticketId, commentDTO.toEntity());
        if(ticketEntity == null){
            throw new NoSuchTicketException("Ticket mit ID: " + ticketId + " wurde nicht gefunden.");
        }
        return ticketEntity.toDto();
    }

    @Override
    public TicketDTO createTicket(TicketDTO ticketDTO) {
        TicketEntity ticketEntity = ticketDBService.createTicket(ticketDTO.toEntity());
        return ticketEntity.toDto();
    }

    @Override
    public void createMasterTicket(Long masterTicketId, List<Long> childTickets) throws NoSuchTicketException{
        ticketDBService.createMasterTicket(masterTicketId, childTickets);
    }

    @Override
    public void createMasterTicket(Long masterTicketId, Long childTicketId) throws NoSuchTicketException {
        ticketDBService.createMasterTicket(masterTicketId, childTicketId);
    }
}
