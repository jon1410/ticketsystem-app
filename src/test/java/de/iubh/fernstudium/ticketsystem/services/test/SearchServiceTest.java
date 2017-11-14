package de.iubh.fernstudium.ticketsystem.services.test;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.TicketDBService;
import de.iubh.fernstudium.ticketsystem.db.services.UserDBService;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.services.SearchService;
import de.iubh.fernstudium.ticketsystem.services.impl.SearchServiceImpl;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceTest {

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @ObjectUnderTest(implementation = SearchServiceImpl.class)
    SearchService searchService;

    @Inject
    private TicketDBService ticketDBService;
    @Inject
    private UserDBService userDBService;

    @Test
    public void testSearchById() throws ExecutionException, InterruptedException {
        when(ticketDBService.getTicketById(anyLong())).thenReturn(buildTicketEntity());
        Future<TicketDTO> dto = searchService.searchById(1L);
        assertNotNull(dto.get());
    }

    @Test
    public void testSearchByReporter() throws ExecutionException, InterruptedException {
        when(userDBService.findById(anyString())).thenReturn(buildUserEntity());
        when(ticketDBService.getTicketsReportedByUserId(any(UserEntity.class))).thenReturn(buildTicketEntityList(3));
        Future<List<TicketDTO>> resultList = searchService.searchByReporter("test");
        assertTrue(resultList.get().size() == 3);
    }

    @Test
    public void testSearchByReporterWithNull() throws ExecutionException, InterruptedException {
        when(userDBService.findById(anyString())).thenReturn(null);
        when(ticketDBService.searchByReporter(anyString())).thenReturn(buildTicketEntityList(3));
        Future<List<TicketDTO>> resultList = searchService.searchByReporter("test");
        assertTrue(resultList.get().size() == 3);
    }

    @Test
    public void testSearchByReporterEmptyList() throws ExecutionException, InterruptedException {
        when(userDBService.findById(anyString())).thenReturn(null);
        when(ticketDBService.searchByReporter(anyString())).thenReturn(null);
        Future<List<TicketDTO>> resultList = searchService.searchByReporter("test");
        assertTrue(resultList.get().size() == 0);
    }

    @Test
    public void testSearchByAssignee() throws ExecutionException, InterruptedException {
        when(userDBService.findById(anyString())).thenReturn(buildUserEntity());
        when(ticketDBService.getTicketsForUserId(any(UserEntity.class))).thenReturn(buildTicketEntityList(3));
        Future<List<TicketDTO>> resultList = searchService.searchByAssignee("test");
        assertTrue(resultList.get().size() == 3);
    }

    @Test
    public void testSearchByAssigneeWithNull() throws ExecutionException, InterruptedException {
        when(userDBService.findById(anyString())).thenReturn(null);
        when(ticketDBService.searchByAssignee(anyString())).thenReturn(buildTicketEntityList(3));
        Future<List<TicketDTO>> resultList = searchService.searchByAssignee("test");
        assertTrue(resultList.get().size() == 3);
    }

    @Test
    public void testSearchByStatus() throws ExecutionException, InterruptedException {
        when(ticketDBService.searchByStatus(any(TicketStatus.class))).thenReturn(buildTicketEntityList(3));
        Future<List<TicketDTO>> resultList = searchService.searchByStatus(TicketStatus.NEW);
        assertTrue(resultList.get().size() == 3);
    }

    @Test
    public void testSearchByStatusNull() throws ExecutionException, InterruptedException {
        when(ticketDBService.searchByStatus(any(TicketStatus.class))).thenReturn(null);
        Future<List<TicketDTO>> resultList = searchService.searchByStatus(TicketStatus.NEW);
        assertTrue(resultList.get().size() == 0);
    }

    @Test
    public void testSearchByTitle() throws ExecutionException, InterruptedException {
        when(ticketDBService.searchByTitle(anyString())).thenReturn(buildTicketEntityList(3));
        Future<List<TicketDTO>> resultList = searchService.searchByTitle("test");
        assertTrue(resultList.get().size() == 3);
    }

    @Test
    public void testSearchByDescription() throws ExecutionException, InterruptedException {
        when(ticketDBService.searchByDescription(anyString())).thenReturn(buildTicketEntityList(3));
        Future<List<TicketDTO>> resultList = searchService.searchByDescription("test");
        assertTrue(resultList.get().size() == 3);
    }

    @Test
    public void testSearchByQuery() throws ExecutionException, InterruptedException {
        when(ticketDBService.searchByCustomQuery(anyString(), anyList())).thenReturn(buildTicketEntityList(3));
        Future<List<TicketDTO>> resultList = searchService.searchByQuery("test", new ArrayList<>());
        assertTrue(resultList.get().size() == 3);
    }

    @Test
    public void testSearchByQueryNull() throws ExecutionException, InterruptedException {
        when(ticketDBService.searchByCustomQuery(anyString(), anyList())).thenReturn(null);
        Future<List<TicketDTO>> resultList = searchService.searchByQuery("test", new ArrayList<>());
        assertTrue(resultList.get().size() == 0);
    }

    private List<TicketEntity> buildTicketEntityList(int size) {
        List<TicketEntity> ticketEntityList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ticketEntityList.add(buildTicketEntity());
        }
        return ticketEntityList;
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
