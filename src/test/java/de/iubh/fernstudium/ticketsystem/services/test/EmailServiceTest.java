package de.iubh.fernstudium.ticketsystem.services.test;

import de.iubh.fernstudium.ticketsystem.services.EmailService;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import de.iubh.fernstudium.ticketsystem.services.impl.EmailServiceImpl;
import de.iubh.fernstudium.ticketsystem.services.impl.TicketServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import javax.inject.Inject;
import javax.mail.Session;

import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @ObjectUnderTest(implementation = EmailServiceImpl.class)
    EmailService emailService;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendEmail(){
        assertFalse(emailService.sendEmail());
    }

    @Test
    public void testIsMailConfigured(){
        assertFalse(emailService.isMailConfigured());
    }
}
