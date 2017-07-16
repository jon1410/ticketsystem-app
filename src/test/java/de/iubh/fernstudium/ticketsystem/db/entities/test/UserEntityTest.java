package de.iubh.fernstudium.ticketsystem.db.entities.test;

import de.iubh.fernstudium.ticketsystem.db.util.JPAHibernateTestManager;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.entities.UserEntity;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by ivanj on 16.07.2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserEntityTest extends JPAHibernateTestManager {

    @Before
    public void acreateUser(){

        UserEntity ue = new UserEntity("testUser", "test", "test",
                "test", UserRole.AD, "test@tst.de");
        assertNotNull(ue);
        em.persist(ue);
    }

    @Test
    public void bgetUser(){
        UserEntity ue = em.find(UserEntity.class, "testUser");
        assertNotNull(ue);
        assertEquals("test", ue.getFirstName());
    }
}
