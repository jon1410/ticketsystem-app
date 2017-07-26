package de.iubh.fernstudium.ticketsystem.db.services.impl;

import de.iubh.fernstudium.ticketsystem.db.entities.CommentEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.services.TicketDBService;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Stateless
public class TicketDBServiceImpl implements TicketDBService{

    @PersistenceContext
    private EntityManager em;

    @Override
    public TicketEntity getTicketById(Long id) {
        return em.find(TicketEntity.class, id);
    }

    @Override
    public List<TicketEntity> getOpenTicketsForUserId(String userId) {
        List<TicketStatus> statusList = getStatusListForOpenTickets();
        TypedQuery<TicketEntity> query = em.createNamedQuery("getTicketsForUserIdAndStatus", TicketEntity.class)
                .setParameter("userid", userId).setParameter("statusList", statusList);

        return query.getResultList();
    }

    @Override
    public boolean changeStauts(Long ticketId, TicketStatus newStatus) {
        TicketEntity ticketEntity = this.getTicketById(ticketId);
        if(ticketEntity == null){
            return false;
        }
        ticketEntity.setTicketStatus(newStatus);
        return true;
    }

    @Override
    public boolean addComment(Long ticketId, CommentEntity comment) {
        TicketEntity ticketEntity = this.getTicketById(ticketId);
        if(ticketEntity == null){
            return false;
        }
        List<CommentEntity> comments = ticketEntity.getComments();
        if(comments == null){
            comments = new ArrayList<>();
        }
        comments.add(comment);
        ticketEntity.setComments(comments);
        return true;
    }

    @Override
    public TicketEntity createTicket(TicketEntity ticketEntity) {
        em.persist(ticketEntity);
        return ticketEntity;
    }

    private List<TicketStatus> getStatusListForOpenTickets() {
        return Arrays.asList(TicketStatus.NEW, TicketStatus.IPS, TicketStatus.IPU, TicketStatus.RES);
    }

}
