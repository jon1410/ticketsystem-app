package de.iubh.fernstudium.ticketsystem.beans.test;

import de.iubh.fernstudium.ticketsystem.beans.CategoryBean;
import de.iubh.fernstudium.ticketsystem.beans.CategoryRepositoryBean;
import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
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
import org.primefaces.context.RequestContext;

import javax.faces.context.FacesContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(FacesContextUtils.class)
public class CategoryRepositoryNegativeTests {
    //eigene Testklasse, weil statisches Mocken notwendig, Needle funktioniert nicht mit PowerMock

    @InjectMocks
    private CategoryRepositoryBean categoryRepositoryBean;

    @Mock
    private CategoryService categoryService;
    @Mock
    private RequestContext requestContext;
    @Mock
    private FacesContext facesContext;

    @Before
    public void init(){
        categoryRepositoryBean = new CategoryRepositoryBean();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDeleteCategoriesWithCategoryNotFoundException() throws CategoryNotFoundException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());

        Mockito.when(categoryService.deleteCategoryById(Mockito.anyString())).thenThrow(new CategoryNotFoundException("test"));
        PowerMockito.when(FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("Test");
        CategoryDTO categoryDTO = buildDTO();
        categoryRepositoryBean.deleteCategory(categoryDTO);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testDeleteCategoriesWithDTONull() throws CategoryNotFoundException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());

        PowerMockito.when(FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("Test");
        categoryRepositoryBean.deleteCategory(null);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testChangeCategoryWithCategoryNotFoundException() throws CategoryNotFoundException, UserNotExistsException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        Mockito.when(categoryService.changeCategory(Mockito.any(CategoryDTO.class))).thenThrow(new CategoryNotFoundException("test"));
        PowerMockito.when(FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("Test");
        CategoryDTO categoryDTO = buildDTO();
        categoryRepositoryBean.changeCategory(categoryDTO);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testChangeCategoryWithDTONull() throws CategoryNotFoundException, UserNotExistsException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        Mockito.when(categoryService.changeCategory(Mockito.any(CategoryDTO.class))).thenThrow(new CategoryNotFoundException("test"));
        PowerMockito.when(FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("Test");
        categoryRepositoryBean.changeCategory(null);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testChangeCategoryWithUserNotExistsException() throws CategoryNotFoundException, UserNotExistsException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        Mockito.when(categoryService.changeCategory(Mockito.any(CategoryDTO.class))).thenThrow(new UserNotExistsException("Test"));
        PowerMockito.when(FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("Test");
        CategoryDTO categoryDTO = buildDTO();
        categoryRepositoryBean.changeCategory(categoryDTO);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testGetSetMethods(){
        String newCat = "NewCategory";
        String newTutorUserId = "NewTutor";
        categoryRepositoryBean.setNewCategoryName(newCat);
        categoryRepositoryBean.setNewTutorUserId(newTutorUserId);

        assertEquals(newCat, categoryRepositoryBean.getNewCategoryName());
        assertEquals(newTutorUserId, categoryRepositoryBean.getNewTutorUserId());
    }

    private CategoryDTO buildDTO() {
        return new CategoryDTO("Test", "Test",
                new UserDTO("userid", "firstName", "lastName", "pw", UserRole.TU));
    }

}
