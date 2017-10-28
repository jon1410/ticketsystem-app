package de.iubh.fernstudium.ticketsystem.domain.event.payload;

import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;

import java.time.LocalDateTime;

public class HistoryPayload {

    private Long ticketId;
    private String eventName;
    private LocalDateTime eventFired;
    private String userId;

    public HistoryPayload() {
    }

    public HistoryPayload(Long ticketId, String eventName, String userId) {
        init(ticketId, userId);
        this.eventName = eventName;

    }

    public HistoryPayload(Long ticketId, HistoryAction action, String userId) {
        init(ticketId, userId);
        this.eventName = action.getResolvedText();
    }

    private void init(Long ticketId, String userId) {
        this.ticketId = ticketId;
        this.eventFired = LocalDateTime.now();
        this.userId = userId;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDateTime getEventFired() {
        return eventFired;
    }

    public void setEventFired(LocalDateTime eventFired) {
        this.eventFired = eventFired;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HistoryAction getHistoryAction(){
        return HistoryAction.fromString(this.eventName);
    }
}
