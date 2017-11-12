package de.iubh.fernstudium.ticketsystem.services.impl;

import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.TicketDBService;
import de.iubh.fernstudium.ticketsystem.db.services.UserDBService;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.services.SearchService;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Stateless
public class SearchServiceImpl implements SearchService {

    @Inject
    private TicketDBService ticketDBService;
    @Inject
    private UserDBService userDBService;

    @Override
    @Asynchronous
    public Future<TicketDTO> searchById(Long id) {
        TicketEntity t =ticketDBService.getTicketById(id);
        return new AsyncResult<>(t.toDto());
    }

    @Override
    @Asynchronous
    public Future<List<TicketDTO>> searchByReporter(String reporter) {
        return searchByUserId(reporter, UserTyp.REPORTER);
    }

    @Override
    public Future<List<TicketDTO>> searchByAssignee(String assignee) {
        return searchByUserId(assignee, UserTyp.ASSIGNEE);
    }

    @Override
    @Asynchronous
    public Future<List<TicketDTO>> searchByStatus(TicketStatus ticketStatus) {
        List<TicketEntity> tickets = ticketDBService.searchByStatus(ticketStatus);
        return new AsyncResult<>(convertToDtoList(tickets));
    }

    @Override
    @Asynchronous
    public Future<List<TicketDTO>> searchByTitle(String text) {
        List<TicketEntity> tickets = ticketDBService.searchByTitle(text);
        return new AsyncResult<>(convertToDtoList(tickets));
    }

    @Override
    @Asynchronous
    public Future<List<TicketDTO>> searchByDescription(String searchtext) {
        List<TicketEntity> tickets = ticketDBService.searchByDescription(searchtext);
        return new AsyncResult<>(convertToDtoList(tickets));
    }

    @Override
    public Future<List<TicketDTO>> searchByQuery(String query, List<Object> params) {
        List<TicketEntity> tickets;
        tickets = ticketDBService.searchByCustomQuery(query , params);

        List<TicketDTO> returnList;
        if(tickets != null){
            returnList = convertToDtoList(tickets);
        }else{
            returnList = new ArrayList<>();
        }

        return new AsyncResult<>(returnList);
    }

    private Future<List<TicketDTO>> searchByUserId(String userId, UserTyp type) {
        UserEntity userEntity = userDBService.findById(userId);
        List<TicketDTO> returnList;
        List<TicketEntity> tickets;
        if(userEntity == null){
            if(type == UserTyp.REPORTER){
                tickets = ticketDBService.searchByReporter(userId);
            }else{
                tickets = ticketDBService.searchByAssignee(userId);
            }
        }else{
            if(type == UserTyp.REPORTER){
                tickets = ticketDBService.getTicketsReportedByUserId(userEntity);
            }else{
                tickets = ticketDBService.getTicketsForUserId(userEntity);
            }
        }
        if(tickets != null){
            returnList = convertToDtoList(tickets);
        }else{
            returnList = new ArrayList<>();
        }
        return new AsyncResult<>(returnList);
    }

    private List<TicketDTO> convertToDtoList(List<TicketEntity> tickets) {
        if(CollectionUtils.isNotEmpty(tickets)){
            //TODO: Unit Test
            return tickets.stream().map(TicketEntity::toDto).collect(Collectors.toList());
        }
        else{
            return new ArrayList<>();
        }
    }

    private enum UserTyp{ REPORTER, ASSIGNEE}
}
