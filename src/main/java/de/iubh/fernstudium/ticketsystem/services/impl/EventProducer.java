package de.iubh.fernstudium.ticketsystem.services.impl;

import de.iubh.fernstudium.ticketsystem.beans.CurrentUserBean;
import de.iubh.fernstudium.ticketsystem.domain.event.payload.CachePayload;
import de.iubh.fernstudium.ticketsystem.domain.event.payload.HistoryPayload;
import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@Stateless
public class EventProducer {

    @Inject
    private Event<HistoryPayload> historyEvent;
    @Inject
    private Event<CachePayload> cachePayloadEvent;
    @Inject
    private CurrentUserBean currentUserBean;

    public void produceHistoryEvent(HistoryPayload historyPayload){
        historyEvent.fire(historyPayload);
    }

    public void produceHistoryEvent(Long ticketId, HistoryAction historyAction, String details){
        historyEvent.fire(new HistoryPayload(ticketId, historyAction, currentUserBean.createUserDto(), details));
    }

    public void produceCacheEvent(CachePayload cachePayload){
        cachePayloadEvent.fire(cachePayload);
    }
}
