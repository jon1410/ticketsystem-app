package de.iubh.fernstudium.ticketsystem.services.test;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.HistoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.HistoryDBService;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;
import de.iubh.fernstudium.ticketsystem.dtos.HistoryDTO;
import de.iubh.fernstudium.ticketsystem.services.HistoryService;
import de.iubh.fernstudium.ticketsystem.services.impl.HistoryServiceImpl;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HistoryServiceTest {

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @ObjectUnderTest(implementation = HistoryServiceImpl.class)
    private HistoryService historyService;

    @Inject
    private HistoryDBService historyDBService;

    @Test
    public void testGetHistoryForTicketOK(){
        when(historyDBService.getHistoryForTicketId(anyLong())).thenReturn(buildHistoryEntityList());

        List<HistoryDTO> historyDTOS =  historyService.getHistoryForTicket(1L);
        assertTrue(historyDTOS.size() == 5);
    }

    @Test
    public void testGetHistoryForTicketNull(){
        when(historyDBService.getHistoryForTicketId(anyLong())).thenReturn(null);

        List<HistoryDTO> historyDTOS =  historyService.getHistoryForTicket(1L);
        assertTrue(historyDTOS.size() == 0);
    }

    private List<HistoryEntity> buildHistoryEntityList() {

        List<HistoryEntity> historyEntities = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            historyEntities.add(new HistoryEntity(buildTicketEntity(),
                    DateTimeUtil.now(), HistoryAction.CA, "Detail", buildUserEntity()));
        }
        return historyEntities;
    }

    private TicketEntity buildTicketEntity() {
        return new TicketEntity("title", "desc", TicketStatus.NEW, buildUserEntity(),
                DateTimeUtil.now(), buildCategoryEntity(), buildUserEntity(),
                null, null, null);
    }

    private UserEntity buildUserEntity() {
        return new UserEntity("user", "firstName", "lastName", "pw", UserRole.TU);
    }

    private CategoryEntity buildCategoryEntity() {
        return new CategoryEntity("ID", "name", buildUserEntity());
    }
}
