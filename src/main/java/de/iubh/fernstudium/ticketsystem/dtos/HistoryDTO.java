package de.iubh.fernstudium.ticketsystem.dtos;

import de.iubh.fernstudium.ticketsystem.db.entities.HistoryEntity;
import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;

import java.time.LocalDateTime;

public class HistoryDTO {

    private Long id;
    private TicketDTO ticketDTO;
    private LocalDateTime eventTime;
    private HistoryAction action;
    private String details;
    private UserDTO userDTO;

    public HistoryDTO() {
    }

    public HistoryDTO(Long id, TicketDTO ticketDTO, LocalDateTime eventTime, HistoryAction action, String details, UserDTO userDTO) {
        this.id = id;
        this.ticketDTO = ticketDTO;
        this.eventTime = eventTime;
        this.action = action;
        this.details = details;
        this.userDTO = userDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TicketDTO getTicketDTO() {
        return ticketDTO;
    }

    public void setTicketDTO(TicketDTO ticketDTO) {
        this.ticketDTO = ticketDTO;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public HistoryAction getAction() {
        return action;
    }

    public void setAction(HistoryAction action) {
        this.action = action;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public HistoryEntity toEntity(){
        return new HistoryEntity(ticketDTO.toEntity(), DateTimeUtil.localDtToSqlTimestamp(eventTime), action, details, userDTO.toEntity());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistoryDTO that = (HistoryDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (ticketDTO != null ? !ticketDTO.equals(that.ticketDTO) : that.ticketDTO != null) return false;
        if (eventTime != null ? !eventTime.equals(that.eventTime) : that.eventTime != null) return false;
        if (action != that.action) return false;
        if (details != null ? !details.equals(that.details) : that.details != null) return false;
        return userDTO != null ? userDTO.equals(that.userDTO) : that.userDTO == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (ticketDTO != null ? ticketDTO.hashCode() : 0);
        result = 31 * result + (eventTime != null ? eventTime.hashCode() : 0);
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (userDTO != null ? userDTO.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HistoryDTO{" +
                "id=" + id +
                ", ticketDTO=" + ticketDTO +
                ", eventTime=" + eventTime +
                ", action=" + action +
                ", details='" + details + '\'' +
                ", userDTO=" + userDTO +
                '}';
    }
}
