package de.iubh.fernstudium.ticketsystem.mocks.test;

import de.iubh.fernstudium.ticketsystem.domain.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CommentDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import de.iubh.fernstudium.ticketsystem.services.mockups.MockupConstatns;
import de.iubh.fernstudium.ticketsystem.services.mockups.TicketServiceMockup;
import de.iubh.fernstudium.ticketsystem.services.mockups.UserServiceMockup;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtil;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtilImpl;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.needle4j.annotation.InjectIntoMany;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleRule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by ivanj on 11.07.2017.
 */
public class TicketServiceMockupTest {

    @Rule
    public final NeedleRule needleRule = new NeedleRule();

    @ObjectUnderTest(postConstruct = true)
    private TicketServiceMockup ticketServiceMockup;

    @ObjectUnderTest(postConstruct = true)
    @InjectIntoMany
    private UserServiceMockup userServiceMockup;

    @InjectIntoMany
    private PasswordUtil passwordUtil = new PasswordUtilImpl();

    @Test
    public void defaultTicketListTest(){
        assertNotNull(ticketServiceMockup.getDefaultTestTickets());
        assertTrue(ticketServiceMockup.getDefaultTestTickets().size() == 5);
    }

    @Test
    public void getOpenTicketsForUserIdTest(){
        assertNotNull(ticketServiceMockup.getOpenTicketsForUserId(MockupConstatns.SYSDEF_USER));
        assertTrue(ticketServiceMockup.getOpenTicketsForUserId("admin").size() == 5);
    }

    @Test
    public void getTicketByIDTest() throws NoSuchTicketException {
        TicketDTO dto = ticketServiceMockup.getTicketByID(1L);
        assertNotNull(dto);
        assertEquals(1L, (long) dto.getId());
    }

    @Test(expected = NoSuchTicketException.class)
    public void getTicketByIDExceptionTest() throws NoSuchTicketException {
        ticketServiceMockup.getTicketByID(10000L);
    }

    @Test
    public void changeStatusTest() throws NoSuchTicketException {
        ticketServiceMockup.changeStatus(1L, TicketStatus.DEL);
        assertEquals(TicketStatus.DEL, ticketServiceMockup.getTicketByID(1L).getTicketStatus());
        ticketServiceMockup.changeStatus(1L, TicketStatus.NEW);
        assertEquals(TicketStatus.NEW, ticketServiceMockup.getTicketByID(1L).getTicketStatus());
    }

    @Test
    public void getHistoricTicketsByUserIdTest(){
        ticketServiceMockup.changeStatus(1L, TicketStatus.CLO);
        assertTrue(ticketServiceMockup.getHistoricTicketsByUserId("admin").size() == 1);
        ticketServiceMockup.changeStatus(1L, TicketStatus.NEW);
    }

    @Test
    public void addCommentTest() throws UserNotExistsException, NoSuchTicketException {

        CommentDTO commentDTO = new CommentDTO(Calendar.getInstance(),
                userServiceMockup.getUserByUserId("admin"), "A comment", Calendar.getInstance());
        ticketServiceMockup.addComment(1L, commentDTO);
        TicketDTO dto = ticketServiceMockup.getTicketByID(1L);
        assertNotNull(dto);
        assertNotNull(dto.getComments());
        assertTrue(dto.getComments().size() == 1);
    }

    @Test
    public void createTicketTest() throws UserNotExistsException, NoSuchTicketException {
        UserDTO reporter = userServiceMockup.getUserByUserId(MockupConstatns.SYSDEF_USER);
        UserDTO assignee = userServiceMockup.getUserByUserId("admin");
        TicketDTO dto = new TicketDTO(0L, "A Title", "Test-Ticket", reporter,
                Calendar.getInstance(), "Test-Category", assignee, null);
        dto = ticketServiceMockup.createTicket(dto);
        assertTrue(dto.getId() > 0);
        assertNotNull(ticketServiceMockup.getTicketByID(dto.getId()));
    }

}
