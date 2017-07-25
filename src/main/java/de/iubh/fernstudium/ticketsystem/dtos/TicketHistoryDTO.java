package de.iubh.fernstudium.ticketsystem.dtos;

import de.iubh.fernstudium.ticketsystem.domain.HistoryAction;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;

import java.time.LocalDateTime;

public class TicketHistoryDTO {

    private Long id;
    private Long ticketId;
    private LocalDateTime actionTime;
    private HistoryAction historyAction;
    private TicketStatus oldStatus;
    private TicketStatus newStaus;
    private UserDTO changedBy;

    public TicketHistoryDTO() {
    }

    public TicketHistoryDTO(Long id, LocalDateTime actionTime, HistoryAction historyAction, TicketStatus oldStatus, TicketStatus newStaus, UserDTO changedBy) {
        this.id = id;
        this.actionTime = actionTime;
        this.historyAction = historyAction;
        this.oldStatus = oldStatus;
        this.newStaus = newStaus;
        this.changedBy = changedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getActionTime() {
        return actionTime;
    }

    public void setActionTime(LocalDateTime actionTime) {
        this.actionTime = actionTime;
    }

    public HistoryAction getHistoryAction() {
        return historyAction;
    }

    public void setHistoryAction(HistoryAction historyAction) {
        this.historyAction = historyAction;
    }

    public TicketStatus getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(TicketStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public TicketStatus getNewStaus() {
        return newStaus;
    }

    public void setNewStaus(TicketStatus newStaus) {
        this.newStaus = newStaus;
    }

    public UserDTO getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(UserDTO changedBy) {
        this.changedBy = changedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketHistoryDTO that = (TicketHistoryDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (actionTime != null ? !actionTime.equals(that.actionTime) : that.actionTime != null) return false;
        if (historyAction != that.historyAction) return false;
        if (oldStatus != that.oldStatus) return false;
        if (newStaus != that.newStaus) return false;
        return changedBy != null ? changedBy.equals(that.changedBy) : that.changedBy == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (actionTime != null ? actionTime.hashCode() : 0);
        result = 31 * result + (historyAction != null ? historyAction.hashCode() : 0);
        result = 31 * result + (oldStatus != null ? oldStatus.hashCode() : 0);
        result = 31 * result + (newStaus != null ? newStaus.hashCode() : 0);
        result = 31 * result + (changedBy != null ? changedBy.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TicketHistoryDTO{" +
                "id=" + id +
                ", actionTime=" + actionTime +
                ", historyAction=" + historyAction +
                ", oldStatus=" + oldStatus +
                ", newStaus=" + newStaus +
                ", changedBy=" + changedBy +
                '}';
    }
}
