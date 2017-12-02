package de.iubh.fernstudium.ticketsystem.services.test;

import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.UserDBService;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidCredentialsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import de.iubh.fernstudium.ticketsystem.services.impl.UserServiceImpl;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtil;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtilImpl;
import org.hibernate.HibernateException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.needle4j.annotation.InjectIntoMany;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import javax.inject.Inject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @ObjectUnderTest(implementation = UserServiceImpl.class)
    private UserService userService;

    @Inject
    private UserDBService userDBService;
    @InjectIntoMany
    private PasswordUtil passwordUtil = new PasswordUtilImpl();

    @Test
    public void testGetUserByIdExists() throws UserNotExistsException {
        Mockito.when(userDBService.findById(Mockito.anyString())).thenReturn(buildUserEntity());
        UserDTO userDTO = userService.getUserByUserId("admin");
        Assert.assertEquals("admin", userDTO.getFirstName());
        Mockito.verify(userDBService, Mockito.times(1)).findById("admin");
    }

    @Test(expected = UserNotExistsException.class)
    public void testGetUserIdNotExists() throws UserNotExistsException {
        Mockito.when(userDBService.findById(Mockito.anyString())).thenThrow(UserNotExistsException.class);
        UserDTO userDTO = userService.getUserByUserId("xxx");
    }

    @Test(expected = UserNotExistsException.class)
    public void testGetUserIdNotExistsNull() throws UserNotExistsException {
        Mockito.when(userDBService.findById(Mockito.anyString())).thenReturn(null);
        UserDTO userDTO = userService.getUserByUserId("xxx");
    }

    @Test
    public void testCreateUserOK() throws UserAlreadyExistsException {
        boolean b = userService.createUser("admin",
                "admin", "admin",
                "admin", UserRole.AD);
        assertTrue(b);
    }

    @Test
    public void testCreateUserUserNOK() throws UserAlreadyExistsException {
        Mockito.doThrow(new HibernateException("This is an Exception")).when(userDBService).persistUser(Mockito.any(UserEntity.class));
        boolean b = userService.createUser("admin",
                "admin", "admin",
                "admin", UserRole.AD);
        assertFalse(b);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testCreateUserUserAlreadyExsists() throws UserAlreadyExistsException {
        Mockito.when(userDBService.findById(Mockito.anyString())).thenReturn(buildUserEntity());
        userService.createUser("admin",
                "admin", "admin",
                "admin", UserRole.AD);
    }

    @Test
    public void testLoginOK() throws UserNotExistsException, InvalidPasswordException, InvalidCredentialsException {
        String hashedPW = passwordUtil.hashPw("admin");
        Mockito.when(userDBService.findById(Mockito.anyString())).thenReturn(buildUserEntity());
        UserDTO userDTO = userService.login("admin", "admin");
        assertNotNull(userDTO);
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testLoginNOK() throws UserNotExistsException, InvalidPasswordException, InvalidCredentialsException {
        String hashedPW = passwordUtil.hashPw("admin");
        Mockito.when(userDBService.findById(Mockito.anyString())).thenReturn(buildUserEntity());
        userService.login("admin", "mySecretPW");
    }

    @Test
    public void testChangePasswordOK() throws UserNotExistsException, InvalidPasswordException {
        Mockito.when(userDBService.findById(Mockito.anyString())).thenReturn(buildUserEntity());
        boolean b = userService.changePassword("admin", "admin", "admin1");
        assertTrue(b);
    }

    @Test
    public void testChangePasswordNOK() throws UserNotExistsException, InvalidPasswordException {
        Mockito.when(userDBService.findById(Mockito.anyString())).thenReturn(buildUserEntity());
        boolean b = userService.changePassword("admin", "admin1", "admin1");
        assertFalse(b);
    }


    private UserEntity buildUserEntity() {
        return new UserEntity("admin",
                "admin", "admin",
                passwordUtil.hashPw("admin"), UserRole.AD)        ;
    }
}
