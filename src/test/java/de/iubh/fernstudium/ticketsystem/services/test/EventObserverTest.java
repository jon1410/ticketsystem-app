package de.iubh.fernstudium.ticketsystem.services.test;

import de.iubh.fernstudium.ticketsystem.beans.CategoryRepositoryBean;
import de.iubh.fernstudium.ticketsystem.beans.TutorRepositoryBean;
import de.iubh.fernstudium.ticketsystem.db.entities.HistoryEntity;
import de.iubh.fernstudium.ticketsystem.db.services.HistoryDBService;
import de.iubh.fernstudium.ticketsystem.db.services.TicketDBService;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.event.payload.CachePayload;
import de.iubh.fernstudium.ticketsystem.domain.event.payload.HistoryPayload;
import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.impl.EventObserver;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import javax.inject.Inject;

import java.time.LocalDateTime;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventObserverTest {

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @ObjectUnderTest
    EventObserver eventObserver;

    @Inject
    private HistoryDBService historyDBService;
    @Inject
    private TicketDBService ticketDBService;
    @Inject
    private TutorRepositoryBean tutorRepositroyBean;
    @Inject
    private CategoryRepositoryBean categoryRepositoryBean;

    @Test
    public void testObserveHistoryEventCategoryDTO(){
        CachePayload cachePayload = new CachePayload(buildCategoryDTO());
        assertNotNull(cachePayload.getCategoryDTO());
        eventObserver.observeCachePayloadEvent(cachePayload);
        verify(categoryRepositoryBean, times(1)).updateCache(any(CategoryDTO.class));
    }

    @Test
    public void testbserveHistoryEventUserDTO(){
        CachePayload cachePayload = new CachePayload(buildUserDTO());
        assertNotNull(cachePayload.getUserDTO());
        eventObserver.observeCachePayloadEvent(cachePayload);
        verify(categoryRepositoryBean, times(1)).updateCache(any(UserDTO.class));
        verify(tutorRepositroyBean, times(1)).updateCache(any(UserDTO.class));
    }

    @Test
    public void testObserveHistoryEvent(){
        HistoryPayload historyPayload = new HistoryPayload(1L, HistoryAction.AC, buildUserDTO(), "Detail");
        assertNotNull(historyPayload.getUserId());
        TicketDTO ticketDTO = buildTicketDTO();
        when(ticketDBService.getTicketById(anyLong())).thenReturn(ticketDTO.toEntity());
        eventObserver.observeHistoryEvent(historyPayload);
        verify(historyDBService, times(1)).createHistoryEntry(any(HistoryEntity.class));
    }

    private TicketDTO buildTicketDTO() {
        return new TicketDTO(1L, "title", "desc",
                TicketStatus.NEW, buildUserDTO(), LocalDateTime.now(), buildCategoryDTO(), buildUserDTO(),
                null, null, null);
    }

    private CategoryDTO buildCategoryDTO() {
        return new CategoryDTO("Test", "Test",
                buildUserDTO());
    }

    private UserDTO buildUserDTO() {
        return new UserDTO("userid", "firstName", "lastName", "pw", UserRole.TU);
    }
}
