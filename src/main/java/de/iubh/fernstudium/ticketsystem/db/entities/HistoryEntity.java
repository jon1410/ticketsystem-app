package de.iubh.fernstudium.ticketsystem.db.entities;

import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;

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

    public HistoryEntity() {
    }

    public HistoryEntity(TicketEntity ticketEntity, Timestamp eventTime, HistoryAction action) {
        this.ticketEntity = ticketEntity;
        this.eventTime = eventTime;
        this.action = action;
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
}
