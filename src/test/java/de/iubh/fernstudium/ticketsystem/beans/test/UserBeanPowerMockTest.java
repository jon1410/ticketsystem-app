package de.iubh.fernstudium.ticketsystem.beans.test;

import de.iubh.fernstudium.ticketsystem.beans.TutorRepositoryBean;
import de.iubh.fernstudium.ticketsystem.beans.UserBean;
import de.iubh.fernstudium.ticketsystem.beans.UserDataBean;
import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({FacesContextUtils.class})
public class UserBeanPowerMockTest {

    @InjectMocks
    UserBean userBean;

    @Mock
    private UserService userService;
    @Mock
    private TutorRepositoryBean tutorRepositoryBean;

    @Before
    public void init(){
        userBean = new UserBean();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetRoleWithNull(){
        assertEquals(UserRole.ST.getResolvedRoleText(), userBean.getRole());
    }

    @Test
    public void testGetSet(){
        userBean.setMailAdressForNewPw("test@mail.de");
        userBean.setNewPassword("newPw");
        userBean.setRepeatedPassword("repPw");
        userBean.setRole(UserRole.AD.getResolvedRoleText());

        assertEquals("test@mail.de", userBean.getMailAdressForNewPw());
        assertEquals("newPw", userBean.getNewPassword());
        assertEquals("repPw", userBean.getRepeatedPassword());
        assertEquals(UserRole.AD.getResolvedRoleText(), userBean.getRole());
    }

    @Test
    public void testCreateNewUserOK() throws UserAlreadyExistsException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        userBean.setRole(UserRole.TU.getResolvedRoleText());
        userBean.setUserRole(UserRole.TU);
        when(userService.createUser(anyString(), anyString(), anyString(), anyString(), any(UserRole.class))).thenReturn(true);
        userBean.createUser();
        verify(tutorRepositoryBean, times(1)).setTutor(any(UserDTO.class));
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testCreateNewUserFalse() throws UserAlreadyExistsException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        userBean.setRole(UserRole.TU.getResolvedRoleText());
        userBean.setUserRole(UserRole.TU);
        when(userService.createUser(anyString(), anyString(), anyString(), anyString(), any(UserRole.class))).thenReturn(false);
        userBean.createUser();

        verify(tutorRepositoryBean, never()).setTutor(any(UserDTO.class));
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testCreateNewUserWithException() throws UserAlreadyExistsException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        userBean.setRole(UserRole.TU.getResolvedRoleText());
        userBean.setUserRole(UserRole.TU);
        when(userService.createUser(anyString(), anyString(), anyString(), anyString(), any(UserRole.class))).thenThrow(new UserAlreadyExistsException("test"));
        userBean.createUser();

        verify(tutorRepositoryBean, never()).setTutor(any(UserDTO.class));
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testAskForNewPassword() throws UserNotExistsException {
        userBean.setMailAdressForNewPw("test@test.de");
        PowerMockito.mockStatic(FacesContextUtils.class);
        doNothing().when(userService).generateNewPassword(anyString());
        userBean.askForNewPassword();
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testAskForNewPasswordWithException() throws UserNotExistsException {
        userBean.setMailAdressForNewPw("test@test.de");
        PowerMockito.mockStatic(FacesContextUtils.class);
        doThrow(new UserNotExistsException("test")).when(userService).generateNewPassword(anyString());
        userBean.askForNewPassword();
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testRegisterUserOK() throws UserAlreadyExistsException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        userBean.setNewPassword("test");
        userBean.setRepeatedPassword("test");
        when(userService.createUser(anyString(), anyString(), anyString(), anyString(), any(UserRole.class))).thenReturn(true);
        userBean.registerUser();

        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testRegisterUserNOK() throws UserAlreadyExistsException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        userBean.setNewPassword("test");
        userBean.setRepeatedPassword("aaaa");
        when(userService.createUser(anyString(), anyString(), anyString(), anyString(), any(UserRole.class))).thenReturn(true);
        userBean.registerUser();

        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

}
