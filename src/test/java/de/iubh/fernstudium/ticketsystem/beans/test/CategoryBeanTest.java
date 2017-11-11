package de.iubh.fernstudium.ticketsystem.beans.test;

import de.iubh.fernstudium.ticketsystem.beans.CategoryBean;
import de.iubh.fernstudium.ticketsystem.beans.CategoryRepositoryBean;
import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.runners.MockitoJUnitRunner;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(FacesContextUtils.class)
public class CategoryBeanTest {

    @InjectMocks
    private CategoryBean categoryBean;

    @Mock
    private UserService userService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private CategoryRepositoryBean categoryRepositoryBean;

    @Before
    public void init(){
        categoryBean = new CategoryBean();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateCategoryOK() throws UserNotExistsException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        categoryBean.setCategoryId("id1");
        categoryBean.setCategoryName("name");
        UserDTO userDTO = buildUserDTO();
        categoryBean.setTutor(userDTO);
        Mockito.when(userService.getUserByUserId(Mockito.anyString())).thenReturn(userDTO);
        Mockito.when(categoryService.addCategory(Mockito.any(CategoryDTO.class))).thenReturn(true);

        PowerMockito.verifyStatic(VerificationModeFactory.times(0));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testCreateCategoryNOK() throws UserNotExistsException {

        assertNotNull(categoryBean);
        PowerMockito.mockStatic(FacesContextUtils.class);
        PowerMockito.when(FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("Test");
        Mockito.when(userService.getUserByUserId(Mockito.anyString())).thenThrow(new UserNotExistsException("User not exists"));
        categoryBean.setTutorUserId("tutor@ticketsystem.de");
        categoryBean.createCategory();

        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testGetSetTutorIdString(){

        assertNotNull(categoryBean);
        categoryBean.setTutorUserId("tutor");
        assertEquals("tutor", categoryBean.getTutorUserId());
    }

    private UserDTO buildUserDTO() {
        return new UserDTO("tutor", "tutor", "tutor", "tutor", UserRole.TU);
    }

}
