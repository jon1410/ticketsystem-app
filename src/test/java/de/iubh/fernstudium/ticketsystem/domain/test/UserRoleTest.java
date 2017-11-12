package de.iubh.fernstudium.ticketsystem.domain.test;

import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by ivanj on 16.07.2017.
 */
public class UserRoleTest {

    @Test
    public void testUserRoleText(){
        assertEquals("Administrator", UserRole.AD.getResolvedRoleText());
        assertEquals("Tutor", UserRole.TU.getResolvedRoleText());
        assertEquals("Student", UserRole.ST.getResolvedRoleText());
    }

    @Test
    public void testUserRoleValueOf(){
        assertEquals(UserRole.ST, UserRole.fromString("Student"));
        assertEquals(UserRole.TU, UserRole.fromString("Tutor"));
        assertEquals(UserRole.AD, UserRole.fromString("Administrator"));
        assertNull(UserRole.fromString("doesNotExist"));
    }}
