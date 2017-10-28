package de.iubh.fernstudium.ticketsystem.services.impl;

import de.iubh.fernstudium.ticketsystem.domain.event.payload.CachePayload;
import de.iubh.fernstudium.ticketsystem.domain.event.payload.CacheUpdatePayload;
import de.iubh.fernstudium.ticketsystem.domain.event.payload.HistoryPayload;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@Stateless
public class EventProducer {

    @Inject
    private Event<HistoryPayload> historyEvent;
    @Inject
    private Event<CacheUpdatePayload> cacheUpdatePayloadEvent;
    @Inject
    private Event<CachePayload> cachePayloadEvent;

    public void produceHistoryEvent(HistoryPayload historyPayload){
        historyEvent.fire(historyPayload);
    }

    public void produceUpdateCacheEvent(CacheUpdatePayload cacheUpdatePayload){
        cacheUpdatePayloadEvent.fire(cacheUpdatePayload);
    }

    public void produceCacheEvent(CachePayload cachePayload){
        cachePayloadEvent.fire(cachePayload);
    }
}
