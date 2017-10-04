package de.iubh.fernstudium.ticketsystem.services.impl;

import com.sun.org.apache.regexp.internal.REProgram;
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
        return searchByUserId(assignee, UserTyp.REPORTER);
    }

    @Override
    @Asynchronous
    public Future<List<TicketDTO>> searchByStatus(TicketStatus ticketStatus) {
        List<TicketEntity> tickets = ticketDBService.searchByStatus(ticketStatus);
        return new AsyncResult<>(convertToDtoList(tickets));
    }

    @Override
    @Asynchronous
    public Future<List<TicketDTO>> searchByTitleOrDescription(String text) {
        return null;
    }

    private Future<List<TicketDTO>> searchByUserId(String userId, UserTyp type) {
        UserEntity userEntity = userDBService.findById(userId);
        List<TicketDTO> returnList;
        if(userEntity == null){
            returnList = new ArrayList<>();
        }else{
            List<TicketEntity> tickets = null;
            if(type == UserTyp.REPORTER){
                tickets = ticketDBService.searchByReporter(userEntity);
            }else{
                tickets = ticketDBService.searchByAssignee(userEntity);
            }
            returnList = convertToDtoList(tickets);
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
