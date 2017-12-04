package de.iubh.fernstudium.ticketsystem.beans.test;

import de.iubh.fernstudium.ticketsystem.beans.CategoryRepositoryBean;
import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
import org.powermock.reflect.Whitebox;
import org.primefaces.context.RequestContext;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FacesContextUtils.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@PowerMockIgnore("javax.management.*")
public class CategoryRepositoryBeanNeedleTest {

    private static List<CategoryDTO> testList;

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @ObjectUnderTest
    CategoryRepositoryBean categoryRepositoryBean;

    @Inject
    private CategoryService categoryService;
    @Inject
    private UserService userService;
    @Mock
    private RequestContext requestContext;
    @Mock
    private FacesContext facesContext;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        testList = new ArrayList<>(5);
        for(int i=0; i<5; i++){
            testList.add(new CategoryDTO("id" + i, "kat" + i , buildUserDTO("Tutor" + i)));
        }
        assertTrue(testList.size() > 0);
        when(categoryService.getAllCategories()).thenReturn(testList);
        categoryRepositoryBean.initCategories();
    }

    @Test
    public void test1GetAllCateogories(){
        assertTrue(categoryRepositoryBean.getAllCategories().size() == 5);
    }

    @Test
    public void test2ChangeCategory() throws UserNotExistsException, CategoryNotFoundException {

        PowerMockito.mockStatic(FacesContextUtils.class);
        Whitebox.setInternalState(categoryRepositoryBean, "allCategories", testList);

        CategoryDTO dto = new CategoryDTO("id1", "kat1" , buildUserDTO("Tutor1"));
        CategoryDTO dtoChange = new CategoryDTO("id1", "change1" , buildUserDTO("Tutor1"));
        when(categoryService.changeCategory(Mockito.any(CategoryDTO.class))).thenReturn(dtoChange);
        categoryRepositoryBean.changeCategory(dto);

        List<CategoryDTO> list = categoryRepositoryBean.getAllCategories();
        dto = findDtoInList(list, "id1");
        assertNotNull(dto);
        assertEquals("change1", dto.getCategoryName());
    }

    @Test
    public void test21ChangeCategoryNoParams() throws UserNotExistsException, CategoryNotFoundException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        Whitebox.setInternalState(categoryRepositoryBean, "allCategories", testList);
        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());
        doNothing().when(requestContext).execute(anyString());

        CategoryDTO dto = new CategoryDTO("id1", "kat1" , buildUserDTO("Tutor1"));
        categoryRepositoryBean.setCurrentDTO(dto);
        categoryRepositoryBean.setNewTutorUserId("Tutor1");
        categoryRepositoryBean.setNewCategoryName("change1");

        CategoryDTO dtoChange = new CategoryDTO("id1", "change1" , buildUserDTO("Tutor1"));
        when(categoryService.changeCategory(Mockito.any(CategoryDTO.class))).thenReturn(dtoChange);
        when(userService.getUserByUserId(anyString())).thenReturn(buildUserDTO("Tutor1"));

        categoryRepositoryBean.changeCategory();

        List<CategoryDTO> list = categoryRepositoryBean.getAllCategories();
        dto = findDtoInList(list, "id1");
        assertNotNull(dto);
        assertEquals("change1", dto.getCategoryName());

        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void test22ChangeCategoryNoParamsDtoNull() throws UserNotExistsException, CategoryNotFoundException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        Whitebox.setInternalState(categoryRepositoryBean, "allCategories", testList);

        categoryRepositoryBean.changeCategory();

        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

    }

    @Test
    public void test23ChangeCategoryNoParamsNotATutor() throws UserNotExistsException, CategoryNotFoundException {

        PowerMockito.mockStatic(FacesContextUtils.class);
        Whitebox.setInternalState(categoryRepositoryBean, "allCategories", testList);
        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());
        doNothing().when(requestContext).execute(anyString());

        UserDTO userDTO = buildUserDTO("Tutor1");

        CategoryDTO dto = new CategoryDTO("id1", "kat1" , userDTO);
        categoryRepositoryBean.setCurrentDTO(dto);
        categoryRepositoryBean.setNewTutorUserId("TutorX");
        categoryRepositoryBean.setNewCategoryName("change1");

        userDTO.setUserRole(UserRole.ST);
        CategoryDTO dtoChange = new CategoryDTO("id1", "change1" , userDTO);
        when(categoryService.changeCategory(Mockito.any(CategoryDTO.class))).thenReturn(dtoChange);
        when(userService.getUserByUserId(anyString())).thenReturn(userDTO);

        categoryRepositoryBean.changeCategory();

        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void test24ChangeCategoryNoParamsException() throws UserNotExistsException, CategoryNotFoundException {

        PowerMockito.mockStatic(FacesContextUtils.class);
        Whitebox.setInternalState(categoryRepositoryBean, "allCategories", testList);
        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());
        doNothing().when(requestContext).execute(anyString());

        UserDTO userDTO = buildUserDTO("Tutor1");

        CategoryDTO dto = new CategoryDTO("id1", "kat1" , userDTO);
        categoryRepositoryBean.setCurrentDTO(dto);
        categoryRepositoryBean.setNewTutorUserId("TutorX");
        categoryRepositoryBean.setNewCategoryName("change1");

        userDTO.setUserRole(UserRole.ST);
        CategoryDTO dtoChange = new CategoryDTO("id1", "change1" , userDTO);
        when(categoryService.changeCategory(Mockito.any(CategoryDTO.class))).thenReturn(dtoChange);
        when(userService.getUserByUserId(anyString())).thenThrow(new UserNotExistsException("test"));

        categoryRepositoryBean.changeCategory();

        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void test3AddNewCategory(){
        Whitebox.setInternalState(categoryRepositoryBean, "allCategories", testList);
        assertTrue(categoryRepositoryBean.getAllCategories().size() == 5);
        CategoryDTO dto = new CategoryDTO("idx", "katx" , buildUserDTO("Tutorx"));

        categoryRepositoryBean.addNewCategory(dto);

        assertTrue(categoryRepositoryBean.getAllCategories().size() == 6);
    }

    @Test
    public void test4DeleteCategoryOK() throws CategoryNotFoundException {
        Whitebox.setInternalState(categoryRepositoryBean, "allCategories", testList);
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());

        when(categoryService.deleteCategoryById(anyString())).thenReturn(true);
        CategoryDTO dto = new CategoryDTO("id1", "kat1" , buildUserDTO("Tutor1"));
        categoryRepositoryBean.deleteCategory(dto);

        assertTrue(categoryRepositoryBean.getAllCategories().size() == 4);
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void test5UpdateCacheUserDTO(){
        CategoryDTO cDto = testList.get(0);

        UserDTO uDto = testList.get(0).getTutor();
        uDto.setFirstName("testChangeFirstName");
        uDto.setLastName("testChangeLastName");

        String catId = cDto.getCategoryId();
        String userId = uDto.getUserId();
        categoryRepositoryBean.updateCache(uDto);

        cDto = findDtoInList(categoryRepositoryBean.getAllCategories(), catId);
        assertNotNull(cDto);
        assertEquals("testChangeFirstName", cDto.getTutor().getFirstName());
        assertEquals("testChangeLastName", cDto.getTutor().getLastName());
    }

    @Test
    public void test6UpdateCacheCategoryDTO(){
        CategoryDTO dto = testList.get(0);
        String id = dto.getCategoryId();
        dto.setCategoryName("change1");
        categoryRepositoryBean.updateCache(dto);
        dto = findDtoInList(categoryRepositoryBean.getAllCategories(), id);
        assertEquals("change1", dto.getCategoryName());
    }


    private static UserDTO buildUserDTO(String tutor1) {
        return new UserDTO(tutor1, "firstName", "lastName", "pass", UserRole.TU);
    }


    private CategoryDTO findDtoInList(List<CategoryDTO> list, String key) {
        for(CategoryDTO c : list){
            if(c.getCategoryId().equals(key)){
                return c;
            }
        }
        return null;
    }
}
