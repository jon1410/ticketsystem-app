package de.iubh.fernstudium.ticketsystem.services.impl;

import de.iubh.fernstudium.ticketsystem.db.entities.HistoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.services.HistoryDBService;
import de.iubh.fernstudium.ticketsystem.db.services.TicketDBService;
import de.iubh.fernstudium.ticketsystem.domain.history.HistoryPayload;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class HistroyEventObserver {

    @Inject
    private HistoryDBService historyDBService;
    @Inject
    private TicketDBService ticketDBService;

    public void observeHistoryEvent(@Observes HistoryPayload historyPayload) {
        TicketEntity t = ticketDBService.getTicketById(historyPayload.getTicketId());
        HistoryEntity historyEntity = new HistoryEntity(t, DateTimeUtil.localDtToSqlTimestamp(historyPayload.getEventFired()), historyPayload.getHistoryAction());
        historyDBService.createHistoryEntry(historyEntity);
    }
}
