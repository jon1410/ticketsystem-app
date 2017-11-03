package de.iubh.fernstudium.ticketsystem.services.impl;

import de.iubh.fernstudium.ticketsystem.beans.CategoryRepositoryBean;
import de.iubh.fernstudium.ticketsystem.beans.CurrentUserBean;
import de.iubh.fernstudium.ticketsystem.beans.TutorRepositoryBean;
import de.iubh.fernstudium.ticketsystem.db.entities.HistoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.HistoryDBService;
import de.iubh.fernstudium.ticketsystem.db.services.TicketDBService;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.event.payload.CachePayload;
import de.iubh.fernstudium.ticketsystem.domain.event.payload.HistoryPayload;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;

import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

@Singleton
public class EventObserver {

    @Inject
    private HistoryDBService historyDBService;
    @Inject
    private TicketDBService ticketDBService;
    @Inject
    private TutorRepositoryBean tutorRepositroyBean;
    @Inject
    private CategoryRepositoryBean categoryRepositoryBean;

    @Asynchronous
    @Lock(LockType.READ)
    public void observeHistoryEvent(@Observes(during = TransactionPhase.AFTER_COMPLETION) HistoryPayload historyPayload) {
        TicketEntity t = ticketDBService.getTicketById(historyPayload.getTicketId());
        UserEntity userEntity = historyPayload.getUserId().toEntity();
        HistoryEntity historyEntity = new HistoryEntity(t, DateTimeUtil.localDtToSqlTimestamp(historyPayload.getEventFired()),
                historyPayload.getHistoryAction(), historyPayload.getDetails(), userEntity);
        historyDBService.createHistoryEntry(historyEntity);
    }

    @Asynchronous
    @Lock(LockType.READ)
    public void observeCachePayloadEvent(@Observes(during = TransactionPhase.AFTER_COMPLETION) CachePayload cachePayload) {

        if(cachePayload.getUserDTO() != null){
            UserDTO userDTO = cachePayload.getUserDTO();
            if(userDTO.getUserRole() == UserRole.TU){
                tutorRepositroyBean.updateCache(userDTO);
                categoryRepositoryBean.updateCache(userDTO);
            }
        }

        if(cachePayload.getCategoryDTO() != null){
            CategoryDTO categoryDTO = cachePayload.getCategoryDTO();
            categoryRepositoryBean.updateCache(categoryDTO);
        }

    }
}
