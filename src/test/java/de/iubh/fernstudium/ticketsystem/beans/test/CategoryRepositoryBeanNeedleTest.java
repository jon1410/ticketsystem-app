package de.iubh.fernstudium.ticketsystem.beans.test;

import de.iubh.fernstudium.ticketsystem.beans.CategoryRepositoryBean;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;
import org.powermock.reflect.Whitebox;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryRepositoryBeanNeedleTest {

    private static List<CategoryDTO> testList;

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @ObjectUnderTest
    CategoryRepositoryBean categoryRepositoryBean;

    @Inject
    private CategoryService categoryService;

    @BeforeClass
    public static void initList(){
        testList = new ArrayList<>(5);
        for(int i=0; i<5; i++){
            testList.add(new CategoryDTO("id" + i, "kat" + i , buildUserDTO("Tutor" + i)));
        }
    }

    @Before
    public void init(){
        assertNotNull(testList);
        assertTrue(testList.size() > 0);
        Mockito.when(categoryService.getAllCategories()).thenReturn(testList);
        categoryRepositoryBean.initCategories();
    }

    @Test
    public void test1GetAllCateogories(){
        assertTrue(categoryRepositoryBean.getAllCategories().size() == 5);
    }

    @Test
    public void test2ChangeCategory() throws UserNotExistsException, CategoryNotFoundException {

        Whitebox.setInternalState(categoryRepositoryBean, "allCategories", testList);

        CategoryDTO dto = new CategoryDTO("id1", "kat1" , buildUserDTO("Tutor1"));
        CategoryDTO dtoChange = new CategoryDTO("id1", "change1" , buildUserDTO("Tutor1"));
        Mockito.when(categoryService.changeCategory(Mockito.any(CategoryDTO.class))).thenReturn(dtoChange);
        categoryRepositoryBean.changeCategory(dto);

        List<CategoryDTO> list = categoryRepositoryBean.getAllCategories();
        dto = findDtoInList(list, "id1");
        assertNotNull(dto);
        assertEquals("change1", dto.getCategoryName());
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
    public void test4DeleteCategoryOK(){
        Whitebox.setInternalState(categoryRepositoryBean, "allCategories", testList);
        //weil vorher eine Kategorie zur Liste hinzugefühgt wurde (Liste ist static, liegt nicht am Stack!
        assertTrue(categoryRepositoryBean.getAllCategories().size() == 6);

        CategoryDTO dto = new CategoryDTO("idx", "katx" , buildUserDTO("Tutorx"));
        categoryRepositoryBean.deleteCategory(dto);

        assertTrue(categoryRepositoryBean.getAllCategories().size() == 5);
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
