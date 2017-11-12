package de.iubh.fernstudium.ticketsystem.services.impl;

import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.services.TicketDBService;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CommentDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.apache.commons.collections.CollectionUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<TicketEntity> ticketEntities = ticketDBService.getOpenTicketsForUserId(userDTO.toEntity());
        return convertToDtoList(ticketEntities);
    }

    @Override
    public List<TicketDTO> getTicketsReportedByUserId(String userId) throws UserNotExistsException {
        UserDTO userDTO = userService.getUserByUserId(userId);
        List<TicketEntity> ticketEntities = ticketDBService.getTicketsReportedByUserId(userDTO.toEntity());
        return convertToDtoList(ticketEntities);
    }

    @Override
    public List<TicketDTO> getHistoricTicketsByUserId(String userId) {
        //TODO evtl. in HistoryService verschieben bzw. kann über Suchfunktion abgebildet werden
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
    public TicketDTO createMasterTicket(Long masterTicketId, List<Long> childTickets) throws NoSuchTicketException{
        TicketEntity ticketEntity = ticketDBService.createMasterTicket(masterTicketId, childTickets);
        return ticketEntity.toDto();
    }

    @Override
    public TicketDTO createMasterTicket(Long masterTicketId, Long childTicketId) throws NoSuchTicketException {
        TicketEntity ticketEntity =ticketDBService.createMasterTicket(masterTicketId, childTicketId);
        return ticketEntity.toDto();
    }

    private List<TicketDTO> convertToDtoList(List<TicketEntity> ticketEntities){
        if(CollectionUtils.isEmpty(ticketEntities)){
            return new ArrayList<>();
        }
        return ticketEntities.stream().map(TicketEntity::toDto).collect(Collectors.toList());
    }
}
