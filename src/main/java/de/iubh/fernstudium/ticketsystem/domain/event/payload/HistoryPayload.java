package de.iubh.fernstudium.ticketsystem.domain.event.payload;

import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;

import java.time.LocalDateTime;

public class HistoryPayload {

    private Long ticketId;
    private String eventName;
    private LocalDateTime eventFired;
    private UserDTO userId;

    public HistoryPayload() {
    }

    public HistoryPayload(Long ticketId, String eventName, UserDTO userId) {
        init(ticketId, userId);
        this.eventName = eventName;

    }

    public HistoryPayload(Long ticketId, HistoryAction action, UserDTO userId) {
        init(ticketId, userId);
        this.eventName = action.getResolvedText();
    }

    private void init(Long ticketId, UserDTO userId) {
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

    public UserDTO getUserId() {
        return userId;
    }

    public void setUserId(UserDTO userId) {
        this.userId = userId;
    }

    public HistoryAction getHistoryAction(){
        return HistoryAction.fromString(this.eventName);
    }
}
