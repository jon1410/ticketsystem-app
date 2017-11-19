package de.iubh.fernstudium.ticketsystem.db.test;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.HistoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HistoryEntityTest {

    @Test
    public void testGetSet(){

        HistoryEntity historyEntity = new HistoryEntity();

        historyEntity.setAction(HistoryAction.CA);
        assertEquals(HistoryAction.CA, historyEntity.getAction());

        historyEntity.setDetails("Details");
        assertEquals("Details", historyEntity.getDetails());

        historyEntity.setEventTime(DateTimeUtil.now());
        assertNotNull(historyEntity.getEventTime());

        historyEntity.setId(1L);
        assertEquals(new Long(1L), historyEntity.getId());

        historyEntity.setTicketEntity(buildTicketEntity());
        assertNotNull(historyEntity.getTicketEntity());

        historyEntity.setUserEntity(buildUserEntity());
        assertNotNull(historyEntity.getUserEntity());

        assertNotNull(historyEntity.toDto());

    }

    private TicketEntity buildTicketEntity() {
        return new TicketEntity("ticket", "ticket", TicketStatus.NEW,
                buildUserEntity(), DateTimeUtil.now(), buildCategoryEntity(), buildUserEntity(), null);
    }

    private UserEntity buildUserEntity() {
        return new UserEntity("userid", "first", "last", "pw", UserRole.TU);
    }

    private CategoryEntity buildCategoryEntity() {
        return new CategoryEntity("catID", "name", buildUserEntity());
    }
}
