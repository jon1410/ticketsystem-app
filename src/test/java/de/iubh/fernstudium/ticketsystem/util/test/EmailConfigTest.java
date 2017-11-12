package de.iubh.fernstudium.ticketsystem.util.test;

import de.iubh.fernstudium.ticketsystem.util.config.EmailConfig;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmailConfigTest {

    @Test
    public void testJNDIRef(){
        assertEquals("java:jboss/mail/Default", EmailConfig.JNDI_MAIL);
    }
}
