package de.iubh.fernstudium.ticketsystem.db.services.impl;

import de.iubh.fernstudium.ticketsystem.db.entities.CommentEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.TicketDBService;
import de.iubh.fernstudium.ticketsystem.db.utils.QueryUtils;
import de.iubh.fernstudium.ticketsystem.db.utils.TypedQueryParameters;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;

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
                .setParameter(TypedQueryParameters.USERID, user).setParameter("statusList", statusList);

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
    public List<TicketEntity> getTicketsForUserId(UserEntity user) {
        TypedQuery<TicketEntity> query = em.createNamedQuery("getTicketsForUserId", TicketEntity.class)
                .setParameter(TypedQueryParameters.USERID, user);
        return query.getResultList();
    }

    @Override
    public List<TicketEntity> getTicketsReportedByUserId(UserEntity user) {
        TypedQuery<TicketEntity> query = em.createNamedQuery("getTicketsReportedByUserId", TicketEntity.class)
                .setParameter(TypedQueryParameters.USERID, user);
        return query.getResultList();
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
    public List<TicketEntity> searchByReporter(String reporter) {

        TypedQuery<TicketEntity> query = em.createNamedQuery("searchByReporter", TicketEntity.class)
                .setParameter(TypedQueryParameters.USERID, reporter);
        return query.getResultList();
    }

    @Override
    public List<TicketEntity> searchByAssignee(String assignee) {
        TypedQuery<TicketEntity> query = em.createNamedQuery("searchByAssignee", TicketEntity.class)
                .setParameter(TypedQueryParameters.USERID, assignee);
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
        final String fullTextQuery = QueryUtils.buildFullTextQuery("DESCRIPTION");
        Query query = em.createNativeQuery(fullTextQuery, TicketEntity.class);
        query.setParameter(1, searchtext);
        return query.getResultList();
    }

    @Override
    public List<TicketEntity> searchByCustomQuery(String query, List<Object> params) {
        Query newQuery = em.createNativeQuery(query, TicketEntity.class);

        for(int i=0; i<params.size(); i++){
            Object o = params.get(i);
            newQuery.setParameter(i+1, o);
        }
        return newQuery.getResultList();
    }

    @Override
    public void createMasterTicket(Long masterTicketId, List<Long> childTickets) throws NoSuchTicketException {
        TicketEntity master = this.getTicketById(masterTicketId);
        List<TicketEntity> children = this.getTicketsByIds(childTickets);

        master.setChildTickets(children);
        setMasterTicketToChildren(children, master);
    }


    @Override
    public void createMasterTicket(Long masterTicketId, Long childTicketId) throws NoSuchTicketException {

        TicketEntity master = this.getTicketById(masterTicketId);
        List<TicketEntity> children = checkChildren(master.getChildTickets(), childTicketId);
        if(master.getChildTickets() == null){
            master.getChildTickets().addAll(children);
        }else{
            master.setChildTickets(children);
        }
    }


    private List<TicketEntity> getTicketsByIds(List<Long> childTickets) throws NoSuchTicketException{
        List<TicketEntity> tickets = new ArrayList<>(childTickets.size());
        for(Long id : childTickets){
            TicketEntity t = this.getTicketById(id);
            tickets.add(t);
        }
        return tickets;
    }

    private void setMasterTicketToChildren(List<TicketEntity> children, TicketEntity masterTicket) {
        for(TicketEntity t : children){
            t.setMasterTicket(masterTicket);
        }
    }

    private List<TicketEntity> checkChildren(List<TicketEntity> childTickets, Long childTicketId) {
        TicketEntity child = this.getTicketById(childTicketId);
        if(childTickets != null){
            childTickets.add(child);
        }else{
            childTickets = new ArrayList<>(1);
            childTickets.add(child);
        }
        return childTickets;
    }
}
