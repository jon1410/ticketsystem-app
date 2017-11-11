package de.iubh.fernstudium.ticketsystem.beans.test;

import de.iubh.fernstudium.ticketsystem.beans.CurrentUserBean;
import de.iubh.fernstudium.ticketsystem.beans.LoginBean;
import de.iubh.fernstudium.ticketsystem.beans.UserDataBean;
import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidCredentialsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;
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

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(FacesContextUtils.class)
public class LoginBeanPowerMockTest {

    @InjectMocks
    private LoginBean loginBean;

    @Mock
    private CurrentUserBean currentUserBean;
    @Mock
    private UserDataBean userDataBean;
    @Mock
    private UserService userService;

    @Before
    public void init(){
        loginBean = new LoginBean();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetSetMethods(){
        loginBean.setUserEmail("userMail@usermail.com");
        loginBean.setUserPasswort("pw");

        assertEquals("userMail@usermail.com", loginBean.getUserEmail());
        assertEquals("pw", loginBean.getUserPasswort());
    }

    @Test
    public void testCheckLoginOK() throws UserNotExistsException, InvalidPasswordException, InvalidCredentialsException {
        String expected = "/main.xhtml?faces-redirect=true";
        Mockito.when(userService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(buildUserDTO());

        String s = loginBean.checkLogin();
        assertEquals(expected, s);
        Mockito.verify(currentUserBean, Mockito.times(1)).init(Mockito.any(UserDTO.class));
        Mockito.verify(userDataBean, Mockito.times(1)).init(Mockito.anyString());
    }

    @Test
    public void testCheckLoginUserNotExistsException() throws UserNotExistsException, InvalidPasswordException, InvalidCredentialsException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        Mockito.when(userService.login(Mockito.anyString(), Mockito.anyString())).thenThrow(new UserNotExistsException("test"));
        loginBean.checkLogin();
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testCheckLoginInvalidPasswordException() throws UserNotExistsException, InvalidPasswordException, InvalidCredentialsException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        Mockito.when(userService.login(Mockito.anyString(), Mockito.anyString())).thenThrow(new InvalidPasswordException("test"));
        loginBean.checkLogin();
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testCheckLoginInvalidCredentialsException() throws UserNotExistsException, InvalidPasswordException, InvalidCredentialsException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        Mockito.when(userService.login(Mockito.anyString(), Mockito.anyString())).thenThrow(new InvalidCredentialsException("test"));
        loginBean.checkLogin();
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    private UserDTO buildUserDTO() {
        return new UserDTO("userid", "firstName", "lastName", "pw", UserRole.TU);
    }
}
