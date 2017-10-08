package de.iubh.fernstudium.ticketsystem.db.services.impl;

import de.iubh.fernstudium.ticketsystem.db.entities.CommentEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.TicketDBService;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    public List<TicketEntity> getOpenTicketsForUserId(UserEntity user) {
        List<TicketStatus> statusList = getStatusListForOpenTickets();
        TypedQuery<TicketEntity> query = em.createNamedQuery("getTicketsForUserIdAndStatus", TicketEntity.class)
                .setParameter("userid", user).setParameter("statusList", statusList);

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

    @Override
    public List<TicketEntity> searchByReporter(UserEntity reporter) {

        TypedQuery<TicketEntity> query = em.createNamedQuery("searchByReporter", TicketEntity.class)
                .setParameter("userid", reporter);
        return query.getResultList();
    }

    @Override
    public List<TicketEntity> searchByAssignee(UserEntity assignee) {
        TypedQuery<TicketEntity> query = em.createNamedQuery("searchByAssignee", TicketEntity.class)
                .setParameter("userid", assignee);
        return query.getResultList();
    }

    @Override
    public List<TicketEntity> searchByStatus(TicketStatus status) {
        TypedQuery<TicketEntity> query = em.createNamedQuery("searchByStatus", TicketEntity.class)
                .setParameter("ticketStatus", status);
        return query.getResultList();
    }

    @Override
    public List<TicketEntity> searchByTitle(String searchText) {
        TypedQuery<TicketEntity> query = em.createNamedQuery("searchByTitle", TicketEntity.class)
                .setParameter("title", searchText);
        return query.getResultList();
    }

    @Override
    public List<TicketEntity> searchByDescription(String searchtext) {
        final String fullTextQuery = buildFullTextQuery("DESCRIPTION");
        Query query = em.createNativeQuery(fullTextQuery, TicketEntity.class);
        query.setParameter(1, searchtext);
        return query.getResultList();
    }

    private String buildFullTextQuery(String tableColumn) {

        tableColumn = "(" + tableColumn + ")";
        StringBuilder sb = new StringBuilder();
        sb.append(QueryParameter.SELECT_ALL).append(QueryParameter.FROM).append(QueryParameter.TABLE_NAME)
                .append(QueryParameter.WHERE).append(QueryParameter.MATCH).append(tableColumn).append(" ")
                .append(QueryParameter.AGAINST).append("?").append(QueryParameter.BOOLEAN_MODE);
        return sb.toString();
    }

    class QueryParameter{

        static final String TABLE_NAME = "ticket ";
        static final String SELECT_ALL = "select * ";
        static final String FROM = "from ";
        static final String WHERE = "where ";
        static final String MATCH = "match";
        static final String AGAINST = "against(";
        static final String BOOLEAN_MODE = " in boolean mode);";
    }
}
