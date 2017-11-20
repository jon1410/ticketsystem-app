package de.iubh.fernstudium.ticketsystem.beans.test;

import de.iubh.fernstudium.ticketsystem.beans.SearchBean;
import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import de.iubh.fernstudium.ticketsystem.services.SearchService;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.primefaces.context.RequestContext;

import javax.faces.context.FacesContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({FacesContextUtils.class})
public class SearchBeanPowerMockTest {

    @InjectMocks
    SearchBean searchBean;

    @Mock
    private SearchService searchService;
    @Mock
    private TicketService ticketService;
    @Mock
    private UserService userService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private RequestContext requestContext;
    @Mock
    private FacesContext facesContext;

    private LocalDateTime creationTime;

    @Before
    public void init(){
        searchBean = new SearchBean();
        MockitoAnnotations.initMocks(this);
        creationTime = LocalDateTime.now();
    }

    @Test
    public void testPostConstruct(){
        searchBean.initStatusValues();
        TicketStatus[] ticketStatus = TicketStatus.values();
        int expectedSize = ticketStatus.length;

        List<String> stati = searchBean.getStati();
        assertEquals(expectedSize, stati.size());

        for(String s : stati){
            assertNotNull(TicketStatus.fromString(s));
        }
    }

    @Test
    public void testSearchSimpleNumeric() throws NoSuchTicketException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        searchBean.setSearchString("123");
        Mockito.when(ticketService.getTicketByID(Mockito.anyLong())).thenReturn(buildTicketDTO());
        searchBean.searchSimple();
        assertNotNull(searchBean.getFoundTickets());
        assertTrue(searchBean.getFoundTickets().size() == 1);
        assertNull(searchBean.getSearchString());
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testSearchSimpleText() throws NoSuchTicketException, InterruptedException, ExecutionException, TimeoutException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        searchBean.setSearchString("test");

        Mockito.when(searchService.searchByTitle(Mockito.anyString())).thenReturn(buildFuture(5));
        Mockito.when(searchService.searchByDescription(Mockito.anyString())).thenReturn(buildFuture(1));

        searchBean.searchSimple();
        assertNotNull(searchBean.getFoundTickets());
        assertTrue(searchBean.getFoundTickets().size() == 1); //weil intern Set verwendet wird und alle DTOs gleich sind
        assertNull(searchBean.getSearchString());

        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testSearchSimpleTextNothingFound() throws NoSuchTicketException, InterruptedException, ExecutionException, TimeoutException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        searchBean.setSearchString("test");

        Mockito.when(searchService.searchByTitle(Mockito.anyString())).thenReturn(buildFuture(0));
        Mockito.when(searchService.searchByDescription(Mockito.anyString())).thenReturn(buildFuture(0));

        searchBean.searchSimple();
        assertNull(searchBean.getFoundTickets());
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testSearchDetailsOnlyWithDates()  {
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());

        String sDate = "Wed Nov 01 12:00:00 CET 2017";
        searchBean.setDateFrom(sDate);
        searchBean.setDateTo(sDate);
        assertEquals(sDate, searchBean.getDateFrom());
        assertEquals(sDate, searchBean.getDateTo());

        Mockito.when(searchService.searchByQuery(Mockito.anyString(), Mockito.anyList())).thenReturn(buildFuture(3));

        searchBean.searchDetails();
        assertNotNull(searchBean.getFoundTickets());
        assertTrue(searchBean.getFoundTickets().size() == 3);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testSearchDetailsDatesAndStatus()  {
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());

        String sDate = "Wed Nov 01 12:00:00 CET 2017";
        searchBean.setDateFrom(sDate);
        searchBean.setDateTo(sDate);
        assertEquals(sDate, searchBean.getDateFrom());
        assertEquals(sDate, searchBean.getDateTo());
        String[] selectedStati = {TicketStatus.NEW.getResolvedText(), TicketStatus.CLO.getResolvedText()};
        searchBean.setSelectedStati(selectedStati);
        assertTrue(searchBean.getSelectedStati().length == 2);

        Mockito.when(searchService.searchByQuery(Mockito.anyString(), Mockito.anyList())).thenReturn(buildFuture(3));

        searchBean.searchDetails();
        assertNotNull(searchBean.getFoundTickets());
        assertTrue(searchBean.getFoundTickets().size() == 3);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testSearchDetailsDatesStatusAndCategory()  {
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());

        String sDate = "Wed Nov 01 12:00:00 CET 2017";
        searchBean.setDateFrom(sDate);
        searchBean.setDateTo(sDate);
        searchBean.setCategoryText("aCategory");
        assertEquals(sDate, searchBean.getDateFrom());
        assertEquals(sDate, searchBean.getDateTo());
        String[] selectedStati = {TicketStatus.NEW.getResolvedText(), TicketStatus.CLO.getResolvedText()};
        searchBean.setSelectedStati(selectedStati);
        assertTrue(searchBean.getSelectedStati().length == 2);

        Mockito.when(searchService.searchByQuery(Mockito.anyString(), Mockito.anyList())).thenReturn(buildFuture(3));
        when(categoryService.getCategoryByName(anyString())).thenReturn(buildCategoryDTOList());

        searchBean.searchDetails();
        assertNotNull(searchBean.getFoundTickets());
        assertTrue(searchBean.getFoundTickets().size() == 3);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testSearchDetailsDatesAndReporterAndAssignee() throws UserNotExistsException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());

        String sDate = "Wed Nov 01 12:00:00 CET 2017";
        searchBean.setDateFrom(sDate);
        searchBean.setDateTo(sDate);
        assertEquals(sDate, searchBean.getDateFrom());
        assertEquals(sDate, searchBean.getDateTo());
        searchBean.setUserIdReporter("testUser");
        assertEquals("testUser", searchBean.getUserIdReporter());
        searchBean.setUserIdAssignee("testUser");
        assertEquals("testUser", searchBean.getUserIdAssignee());

        Mockito.when(userService.getUserByUserId(Mockito.anyString())).thenReturn(buildUserDTO());
        Mockito.when(searchService.searchByQuery(Mockito.anyString(), Mockito.anyList())).thenReturn(buildFuture(3));

        searchBean.searchDetails();
        assertNotNull(searchBean.getFoundTickets());
        assertTrue(searchBean.getFoundTickets().size() == 3);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testSearchDetailsDatesAndReporterAndAssigneeWithException() throws UserNotExistsException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());

        String sDate = "Wed Nov 01 12:00:00 CET 2017";
        searchBean.setDateFrom(sDate);
        searchBean.setDateTo(sDate);
        assertEquals(sDate, searchBean.getDateFrom());
        assertEquals(sDate, searchBean.getDateTo());
        searchBean.setUserIdReporter("testUser");
        assertEquals("testUser", searchBean.getUserIdReporter());
        searchBean.setUserIdAssignee("testUser");
        assertEquals("testUser", searchBean.getUserIdAssignee());

        Mockito.when(userService.getUserByUserId(Mockito.anyString())).thenThrow(new UserNotExistsException("test"));
        Mockito.when(searchService.searchByQuery(Mockito.anyString(), Mockito.anyList())).thenReturn(buildFuture(3));

        searchBean.searchDetails();
        assertNotNull(searchBean.getFoundTickets());
        assertTrue(searchBean.getFoundTickets().size() == 3);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testSearchDetailsNothingFound() throws UserNotExistsException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        String sDate = "Wed Nov 01 12:00:00 CET 2017";
        searchBean.setDateFrom(sDate);
        searchBean.setDateTo(sDate);
        assertEquals(sDate, searchBean.getDateFrom());
        assertEquals(sDate, searchBean.getDateTo());
        searchBean.setUserIdReporter("testUser");
        assertEquals("testUser", searchBean.getUserIdReporter());
        searchBean.setUserIdAssignee("testUser");
        assertEquals("testUser", searchBean.getUserIdAssignee());

        Mockito.when(userService.getUserByUserId(Mockito.anyString())).thenThrow(new UserNotExistsException("test"));
        Mockito.when(searchService.searchByQuery(Mockito.anyString(), Mockito.anyList())).thenReturn(buildFuture(0));

        searchBean.searchDetails();
        assertNull(searchBean.getFoundTickets());
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    private Future<List<TicketDTO>> buildFuture(int size) {
        return CompletableFuture.completedFuture(buildDTOList(size));
    }

    private List<TicketDTO> buildDTOList(int size) {
        List<TicketDTO> tickets = new ArrayList<>(size);
        for(int i=0; i<size; i++){
            tickets.add(buildTicketDTO());
        }
        return tickets;
    }


    private TicketDTO buildTicketDTO() {
        return new TicketDTO(1L, "title", "desc",
                TicketStatus.NEW, buildUserDTO(), creationTime, buildCategoryDTO(), buildUserDTO(),
                null, null, null);
    }

    private CategoryDTO buildCategoryDTO() {
        return new CategoryDTO("cid", "name", buildUserDTO());
    }

    private UserDTO buildUserDTO() {
        return new UserDTO("userid", "firstName", "lastName", "pw", UserRole.TU);
    }

    private List<CategoryDTO> buildCategoryDTOList() {
        List<CategoryDTO> categoryDTOList = new ArrayList<>(5);

        for (int i = 0; i < 5; i++) {
            categoryDTOList.add(new CategoryDTO("c"+i, "name", buildUserDTO()));
        }
        return categoryDTOList;
    }

}
