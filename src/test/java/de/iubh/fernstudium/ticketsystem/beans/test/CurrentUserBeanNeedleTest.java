package de.iubh.fernstudium.ticketsystem.beans.test;

import de.iubh.fernstudium.ticketsystem.beans.CategoryRepositoryBean;
import de.iubh.fernstudium.ticketsystem.beans.CurrentUserBean;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import de.iubh.fernstudium.ticketsystem.services.impl.EventProducer;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import javax.ejb.EJB;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CurrentUserBeanNeedleTest {

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @ObjectUnderTest
    CurrentUserBean currentUserBean;

    @Inject
    private UserService userService;
    @EJB
    private EventProducer eventProducer;

    @Before
    public void init(){
        UserDTO userDTO = buildUserDTO();
        currentUserBean.init(userDTO);
    }

    @Test
    public void testInit(){
        assertEquals("userid", currentUserBean.getUserId());
        assertEquals("firstName", currentUserBean.getFirstName());
        assertEquals("lastName", currentUserBean.getLastName());
        assertEquals("pw", currentUserBean.getPassword());
        assertEquals("Tutor", currentUserBean.getResolvedUserRole());
        assertEquals(UserRole.TU, currentUserBean.getUserRole());
    }

    @Test
    public void testChangeUserData(){
        currentUserBean.setNewFirstName("neuFirst");
        currentUserBean.setNewLastName("neuLast");

        currentUserBean.changeUserData();

        assertEquals("neuFirst", currentUserBean.getFirstName());
        assertEquals("neuLast", currentUserBean.getLastName());

        //in diesen Get-Methoden erfolgt eine Abfrage auf NULL, wenn true, wird der aktuelle Name zurück gegeben
        //daher wissen wir, dass die Values hier null sind, wenn diese Assertion zutrifft
        assertEquals("neuFirst", currentUserBean.getNewFirstName());
        assertEquals("neuLast", currentUserBean.getNewLastName());
    }

    @Test
    public void testCreateUserDTO(){
        UserDTO userDTO = currentUserBean.createUserDto();
        assertEquals("userid", userDTO.getUserId());
        assertEquals("firstName", userDTO.getFirstName());
        assertEquals("lastName", userDTO.getLastName());
        assertEquals("pw", userDTO.getPassword());
        assertEquals(UserRole.TU, userDTO.getUserRole());
    }

    private UserDTO buildUserDTO() {
        return new UserDTO("userid", "firstName", "lastName", "pw", UserRole.TU);
    }
}
