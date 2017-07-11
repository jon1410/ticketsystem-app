package de.iubh.fernstudium.ticketsystem.mocks.test;

import de.iubh.fernstudium.ticketsystem.services.UserService;
import de.iubh.fernstudium.ticketsystem.services.mockups.MockupConstatns;
import de.iubh.fernstudium.ticketsystem.services.mockups.TicketServiceMockup;
import de.iubh.fernstudium.ticketsystem.services.mockups.UserServiceMockup;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtil;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtilImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.needle4j.annotation.InjectIntoMany;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleRule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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


}
