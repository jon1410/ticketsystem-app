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

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(FacesContextUtils.class)
public class CategoryRepositoryNegativeTests {
    //eigene Testklasse, weil statisches Mocken notwendig, Needle funktioniert nicht mit PowerMock

    @InjectMocks
    private CategoryRepositoryBean categoryRepositoryBean;

    @Mock
    private CategoryService categoryService;

    @Before
    public void init(){
        categoryRepositoryBean = new CategoryRepositoryBean();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDeleteCategoriesWithCategoryNotFoundException() throws CategoryNotFoundException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        Mockito.when(categoryService.deleteCategoryById(Mockito.anyString())).thenThrow(new CategoryNotFoundException());
        PowerMockito.when(FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("Test");
        CategoryDTO categoryDTO = buildDTO();
        categoryRepositoryBean.deleteCategory(categoryDTO);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testDeleteCategoriesWithDTONull() throws CategoryNotFoundException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        Mockito.when(categoryService.deleteCategoryById(Mockito.anyString())).thenThrow(new CategoryNotFoundException());
        PowerMockito.when(FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("Test");
        categoryRepositoryBean.deleteCategory(null);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testChangeCategoryWithCategoryNotFoundException() throws CategoryNotFoundException, UserNotExistsException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        Mockito.when(categoryService.changeCategory(Mockito.any(CategoryDTO.class))).thenThrow(new CategoryNotFoundException());
        PowerMockito.when(FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("Test");
        CategoryDTO categoryDTO = buildDTO();
        categoryRepositoryBean.changeCategory(categoryDTO);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testChangeCategoryWithDTONull() throws CategoryNotFoundException, UserNotExistsException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        Mockito.when(categoryService.changeCategory(Mockito.any(CategoryDTO.class))).thenThrow(new CategoryNotFoundException());
        PowerMockito.when(FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("Test");
        categoryRepositoryBean.changeCategory(null);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testChangeCategoryWithUserNotExistsException() throws CategoryNotFoundException, UserNotExistsException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        Mockito.when(categoryService.changeCategory(Mockito.any(CategoryDTO.class))).thenThrow(new UserNotExistsException());
        PowerMockito.when(FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("Test");
        CategoryDTO categoryDTO = buildDTO();
        categoryRepositoryBean.changeCategory(categoryDTO);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    private CategoryDTO buildDTO() {
        return new CategoryDTO("Test", "Test",
                new UserDTO("userid", "firstName", "lastName", "pw", UserRole.TU));
    }

}
