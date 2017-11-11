package de.iubh.fernstudium.ticketsystem.beans.test;

import de.iubh.fernstudium.ticketsystem.beans.SearchBean;
import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
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
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import sun.security.krb5.internal.Ticket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(FacesContextUtils.class)
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
    private Future<List<TicketDTO>> future;

    @Before
    public void init(){
        searchBean = new SearchBean();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPostConstruct(){
        searchBean.initStatusValues();
        TicketStatus[] ticketStatus = TicketStatus.values();
        int expectedSize = ticketStatus.length;

        String[] stati = searchBean.getStati();
        assertEquals(expectedSize, stati.length);

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

        Mockito.when(searchService.searchByTitle(Mockito.anyString())).thenReturn(null);
        Mockito.when(searchService.searchByDescription(Mockito.anyString())).thenReturn(null);
        Mockito.when(future.get(3, TimeUnit.SECONDS)).thenReturn(buildDTOList());


        searchBean.searchSimple();
        assertNotNull(searchBean.getFoundTickets());
        assertTrue(searchBean.getFoundTickets().size() == 1);
        assertNull(searchBean.getSearchString());
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    private List<TicketDTO> buildDTOList() {
        List<TicketDTO> tickets = new ArrayList<>(5);
        for(int i=0; i<tickets.size(); i++){
            tickets.add(buildTicketDTO());
        }
        return tickets;
    }


    private TicketDTO buildTicketDTO() {
        return new TicketDTO(1L, "title", "desc",
                TicketStatus.NEW, buildUserDTO(), LocalDateTime.now(), buildCategoryDTO(), buildUserDTO(),
                null, null, null);
    }

    private CategoryDTO buildCategoryDTO() {
        return new CategoryDTO("cid", "name", buildUserDTO());
    }

    private UserDTO buildUserDTO() {
        return new UserDTO("userid", "firstName", "lastName", "pw", UserRole.TU);
    }
}
