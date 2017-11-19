package de.iubh.fernstudium.ticketsystem.beans.test;

import de.iubh.fernstudium.ticketsystem.beans.CurrentUserBean;
import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import de.iubh.fernstudium.ticketsystem.services.impl.EventProducer;
import org.junit.Before;
import org.junit.Ignore;
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(FacesContextUtils.class)
public class CurrentUserBeanPowerMockTest {

    @InjectMocks
    private CurrentUserBean currentUserBean;

    @Mock
    private UserService userService;
    @Mock
    private EventProducer eventProducer;

    @Before
    public void init(){
        currentUserBean = new CurrentUserBean();
        MockitoAnnotations.initMocks(this);
        UserDTO userDTO = buildUserDTO();
        currentUserBean.init(userDTO);
    }

    @Test
    public void testChangeUserDataNoChanges(){
        PowerMockito.mockStatic(FacesContextUtils.class);
        currentUserBean.setNewFirstName("firstName");
        currentUserBean.setNewLastName("lastName");

        currentUserBean.changeUserData();
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testChangePasswordOK() throws UserNotExistsException, InvalidPasswordException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        currentUserBean.setNewPassword("neuPw");
        currentUserBean.setRepeatedPassword("neuPw");
        Mockito.when(userService.changePassword(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(true);
        currentUserBean.changePassword();

        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        assertNull(currentUserBean.getRepeatedPassword());
        assertNull(currentUserBean.getNewPassword());
    }

    @Test
    public void testChangePwPwNotMatching(){
        PowerMockito.mockStatic(FacesContextUtils.class);
        currentUserBean.setNewPassword("newPw");
        currentUserBean.setRepeatedPassword("xy");

        currentUserBean.changePassword();
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testChangePwNOK() throws UserNotExistsException, InvalidPasswordException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        currentUserBean.setNewPassword("newPw");
        currentUserBean.setRepeatedPassword("newPw");

        Mockito.when(userService.changePassword(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(false);
        currentUserBean.changePassword();
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testChangePwUserNotExistsException() throws UserNotExistsException, InvalidPasswordException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        currentUserBean.setNewPassword("newPw");
        currentUserBean.setRepeatedPassword("newPw");

        Mockito.when(userService.changePassword(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenThrow(new UserNotExistsException("test"));
        currentUserBean.changePassword();
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testChangePwInvalidPasswordException() throws UserNotExistsException, InvalidPasswordException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        currentUserBean.setNewPassword("newPw");
        currentUserBean.setRepeatedPassword("xy");

        Mockito.when(userService.changePassword(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenThrow(new InvalidPasswordException("test"));
        currentUserBean.changePassword();
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testLogoutOK(){
        String logoutString = "/login.xhtml?faces-redirect=true";
        PowerMockito.mockStatic(FacesContextUtils.class);
        String logout = currentUserBean.logout();
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.logout(logoutString);
    }

    @Test
    public void testToString(){
        String s = currentUserBean.toString();
        assertNotNull(s);
    }

    private UserDTO buildUserDTO() {
        return new UserDTO("userid", "firstName", "lastName", "pw", UserRole.TU);
    }
}
