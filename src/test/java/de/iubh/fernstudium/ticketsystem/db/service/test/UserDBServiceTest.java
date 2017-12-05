package de.iubh.fernstudium.ticketsystem.db.service.test;

import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.UserDBService;
import de.iubh.fernstudium.ticketsystem.db.services.impl.UserDBServiceImpl;
import de.iubh.fernstudium.ticketsystem.db.util.JPAHibernateTestManager;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.powermock.reflect.Whitebox;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserDBServiceTest extends JPAHibernateTestManager {

    private UserDBService userDBService = new UserDBServiceImpl();

     @Before
     public void initEntityManager(){
         //initilisiert die Entitmanager-Klasse in den DB-Serivices, baut den
         Whitebox.setInternalState(userDBService, "em", super.em);
     }

    @Test
    public void test1PersistOk(){
         em.getTransaction().begin();
        UserEntity ue = new UserEntity("testUser", "test", "test",
                "test", UserRole.AD);
        userDBService.persistUser(ue);
        UserEntity ue1 = new UserEntity("testUser1", "test", "test",
                "test", UserRole.TU);
        userDBService.persistUser(ue1);
        em.getTransaction().commit();
    }

    @Test
    public void test2FindByIdOk(){
        UserEntity ue = userDBService.findById("testUser");
        assertNotNull(ue);
        Assert.assertEquals("testUser", ue.getUserId());
        Assert.assertEquals("test", ue.getFirstName());
    }

    @Test
    public void test3ChangePasswordOK(){
        UserEntity ue = userDBService.findById("testUser");
        Assert.assertEquals("test", ue.getPassword());
        em.getTransaction().begin();
        userDBService.changePassword("testUser", "newPW");
        em.getTransaction().commit();
        UserEntity ue1 = userDBService.findById("testUser");
        Assert.assertEquals("newPW", ue1.getPassword());
    }

    @Test
    public void test4UpdateUser(){
        userDBService.updateUser("testUser1", "newFirst", "newLast", UserRole.ST);
    }

    @Test
    public void test5DeleteUser(){
        userDBService.deleteUser("testUser1");
    }

    @Test
    public void test6FindByRole(){
        List<UserEntity> userEntities = userDBService.findByRole(UserRole.AD);
        assertNotNull(userEntities);
        assertTrue(userEntities.size() == 1);
    }


}
