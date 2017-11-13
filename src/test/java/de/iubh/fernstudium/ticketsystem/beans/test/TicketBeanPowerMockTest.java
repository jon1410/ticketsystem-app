package de.iubh.fernstudium.ticketsystem.beans.test;

import de.iubh.fernstudium.ticketsystem.beans.CurrentUserBean;
import de.iubh.fernstudium.ticketsystem.beans.TicketBean;
import de.iubh.fernstudium.ticketsystem.beans.UserDataBean;
import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import de.iubh.fernstudium.ticketsystem.services.impl.EventProducer;
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
import org.powermock.reflect.Whitebox;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({FacesContextUtils.class})
public class TicketBeanPowerMockTest {

    @InjectMocks
    TicketBean ticketBean;

    @Mock
    private TicketService ticketService;
    @Mock
    private CurrentUserBean currentUserBean;
    @Mock
    private UserDataBean userDataBean;
    @Mock
    private EventProducer eventProducer;
    @Mock
    private CategoryService categoryService;

    @Before
    public void init(){
        ticketBean = new TicketBean();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateTicketOK() throws CategoryNotFoundException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        CurrentUserBean currentUserBean = new CurrentUserBean();
        currentUserBean.setUserId("userid");
        Whitebox.setInternalState(ticketBean, "currentUserBean", currentUserBean);
        Mockito.when(categoryService.getCategoryById(Mockito.anyString())).thenReturn(buildCategoryDTO());
        Mockito.when(ticketService.createTicket(Mockito.any(TicketDTO.class))).thenReturn(buildTicketDTO());

        ticketBean.setCategoryId("testId");
        assertEquals("testId", ticketBean.getCategoryId());
        ticketBean.setTitle("Test");
        ticketBean.setDescription("Test");
        ticketBean.createTicket();
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    private CategoryDTO buildCategoryDTO() {
        return new CategoryDTO("Test", "Test", buildUserDTO());
    }

    private UserDTO buildUserDTO(){
        return  new UserDTO("userid", "firstName", "lastName", "pw", UserRole.TU);
    }

    private TicketDTO buildTicketDTO() {
        return new TicketDTO(1L, "title", "desc",
                TicketStatus.NEW, buildUserDTO(), LocalDateTime.now(), buildCategoryDTO(), buildUserDTO(),
                null, null, null);
    }
}
