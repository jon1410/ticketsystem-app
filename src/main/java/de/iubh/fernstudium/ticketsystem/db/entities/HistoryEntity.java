package de.iubh.fernstudium.ticketsystem.db.entities;

import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;
import de.iubh.fernstudium.ticketsystem.dtos.HistoryDTO;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "TICKET_HISTORY")
@NamedQueries({
        @NamedQuery(name = "getHistoryForTicketId", query = "select h from HistoryEntity h where h.ticketEntity.id = :ticketId"),
})
public class HistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TicketEntity ticketEntity;

    @Column(name = "EVENT_TIME", nullable = false)
    private Timestamp eventTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "HIST_ACTION", nullable = false)
    private HistoryAction action;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userEntity;

    @Column(name = "ACTION_DETAILS")
    private String details;

    public HistoryEntity() {
    }

    public HistoryEntity(TicketEntity ticketEntity, Timestamp eventTime, HistoryAction action, String details, UserEntity user) {
        this.ticketEntity = ticketEntity;
        this.eventTime = eventTime;
        this.action = action;
        this.details = details;
        this.userEntity = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TicketEntity getTicketEntity() {
        return ticketEntity;
    }

    public void setTicketEntity(TicketEntity ticketEntity) {
        this.ticketEntity = ticketEntity;
    }

    public Timestamp getEventTime() {
        return eventTime;
    }

    public void setEventTime(Timestamp eventTime) {
        this.eventTime = eventTime;
    }

    public HistoryAction getAction() {
        return action;
    }

    public void setAction(HistoryAction action) {
        this.action = action;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public HistoryDTO toDto(){
        return new HistoryDTO(id, ticketEntity.toDto(), DateTimeUtil.sqlTimestampToLocalDate(eventTime),
                action, details, userEntity.toDto());
    }

}
