package de.iubh.fernstudium.ticketsystem.db.services.impl;

import de.iubh.fernstudium.ticketsystem.db.entities.HistoryEntity;
import de.iubh.fernstudium.ticketsystem.db.services.HistoryDBService;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class HistoryDBServiceImpl implements HistoryDBService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void createHistoryEntry(HistoryEntity historyEntity) {
        em.persist(historyEntity);
    }

    @Override
    public List<HistoryEntity> getHistoryForTicketId(Long ticketId) {
        Query query = em.createNamedQuery("getHistoryForTicketId", HistoryEntity.class)
                .setParameter("ticketId", ticketId);
        return query.getResultList();
    }
}
