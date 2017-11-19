package de.iubh.fernstudium.ticketsystem.beans.test;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.primefaces.context.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(FacesContext.class)
public class FacesContextUtilsTest {

    @Mock
    private RequestContext requestContext;
    @Mock
    private FacesContext facesContext;
    @Mock
    private ExternalContext externalContext;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockStatic(FacesContext.class);
        when(FacesContext.getCurrentInstance()).thenReturn(facesContext);
        when(facesContext.getExternalContext()).thenReturn(externalContext);
        doNothing().when(facesContext).addMessage(anyString(), any(FacesMessage.class));
        doNothing().when(externalContext).invalidateSession();
    }

    @Test
    public void testLogoutNull(){

        String s = FacesContextUtils.logout(null);
        assertEquals(FacesContextUtils.REDIRECT_LOGIN, s);

        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testLogout(){

        String s = FacesContextUtils.logout("test");
        assertEquals("test", s);

        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testRedirectWithInformationToMain(){
        String s = FacesContextUtils.redirectWithInformationToMain("test", "test");
        assertEquals(FacesContextUtils.REDIRECT_MAIN, s);
    }

    @Test
    public void testResolveInfo(){
        String s = FacesContextUtils.resolveInfo("test", "test", "return");
        assertEquals("return", s);
    }

    @Test
    public void testResolveError(){
        String s = FacesContextUtils.resolveError("test", "test", "return");
        assertEquals("return", s);
    }
}
