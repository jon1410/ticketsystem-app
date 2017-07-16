package de.iubh.fernstudium.ticketsystem.domain.test;

import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ivanj on 16.07.2017.
 */
public class UserRoleTest {

    @Test
    public void userRoleTextTest(){
        assertEquals("Administrator", UserRole.AD.getResolvedRoleText());
        assertEquals("Tutor", UserRole.TU.getResolvedRoleText());
        assertEquals("Student", UserRole.ST.getResolvedRoleText());
    }
}
