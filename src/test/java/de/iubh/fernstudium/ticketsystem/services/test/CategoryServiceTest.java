package de.iubh.fernstudium.ticketsystem.services.test;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.CategoryDBService;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import de.iubh.fernstudium.ticketsystem.services.impl.CategoryServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @ObjectUnderTest(implementation = CategoryServiceImpl.class)
    CategoryService categoryService;

    @Inject
    private CategoryDBService categoryDBService;

    @Inject
    private UserService userService;

    @Test
    public void testGetCategoryByIdOK() throws CategoryNotFoundException {
        when(categoryDBService.getCategoryById(Mockito.anyString())).thenReturn(buildEntity());
        CategoryDTO dto = categoryService.getCategoryById("test");
        assertNotNull(dto);
        assertEquals("ID", dto.getCategoryId());
    }

    @Test
    public void testChangeTutorOK() throws CategoryNotFoundException, UserNotExistsException {
        UserEntity userEntity = buildUserEntity();
        assertNotNull(userEntity.toDto());
        when(userService.getUserByUserId(Mockito.anyString())).thenReturn(userEntity.toDto());
        when(categoryDBService.changeTutor(Mockito.anyString(), Mockito.any(UserEntity.class))).thenReturn(true);
        categoryService.changeTutor("Test", "newTutor");
        Mockito.verify(categoryDBService, Mockito.times(1)).changeTutor(Mockito.anyString(), Mockito.any(UserEntity.class));
    }

    @Test
    public void testChangeCategoryNameOK() throws CategoryNotFoundException, UserNotExistsException {
        when(categoryDBService.changeCategoryName(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        categoryService.changeCategoryName("test", "newName");
        Mockito.verify(categoryDBService, Mockito.times(1)).changeCategoryName(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testChangeCategoryNull() throws CategoryNotFoundException, UserNotExistsException{
        UserEntity userEntity = buildUserEntity();
        assertNotNull(userEntity.toDto());
        when(userService.getUserByUserId(Mockito.anyString())).thenReturn(userEntity.toDto());
        when(categoryDBService.getCategoryById(Mockito.anyString())).thenReturn(buildEntity());
        when(categoryDBService.changeCategory(Mockito.any(CategoryEntity.class))).thenReturn(buildEntity());
        CategoryEntity categoryEntity = buildEntity();
        CategoryDTO categoryDTO = categoryService.changeCategory(categoryEntity.toDto());
        assertNull(categoryDTO); //weil Entities gleich sind, somit keine Änderung!
    }

    @Test
    public void testChangeCategoryOK() throws CategoryNotFoundException, UserNotExistsException{
        UserEntity userEntity = buildUserEntity();
        assertNotNull(userEntity.toDto());
        CategoryEntity categoryEntity = buildEntity();
        CategoryEntity categoryEntity1 = buildEntity();
        CategoryEntity categoryEntity2 = buildEntity();

        when(userService.getUserByUserId(Mockito.anyString())).thenReturn(userEntity.toDto());
        when(categoryDBService.getCategoryById(Mockito.anyString())).thenReturn(categoryEntity);
        when(categoryDBService.changeCategory(Mockito.any(CategoryEntity.class))).thenReturn(categoryEntity1);

        categoryEntity2.setCategoryName("newName");
        CategoryDTO categoryDTO = categoryService.changeCategory(categoryEntity2.toDto());
        assertNotNull(categoryDTO); //Entities nicht gleich, somit Änderung durchgeführt und neues DTO wird zurückgegeben
    }

    @Test
    public void testDeleteCategoryByIdOK() throws CategoryNotFoundException, UserNotExistsException{
        when(categoryDBService.deleteCategory(Mockito.anyString())).thenReturn(true);
        assertTrue(categoryService.deleteCategoryById("testID"));
    }

    @Test
    public void testAddCategoryOK() throws CategoryNotFoundException, UserNotExistsException{
        when(categoryDBService.addCategory(Mockito.any(CategoryEntity.class))).thenReturn(true);
        CategoryEntity categoryEntity = buildEntity();
        assertTrue(categoryService.addCategory(categoryEntity.toDto()));
    }

    @Test
    public void testGetAllCategories(){
        when(categoryDBService.getAllCategories()).thenReturn(buildList(5));
        List<CategoryDTO> categoryDTOList = categoryService.getAllCategories();
        assertTrue(categoryDTOList.size() == 5);
    }

    @Test
    public void testGetAllCategoriesSizeZero(){
        when(categoryDBService.getAllCategories()).thenReturn(buildList(0));
        List<CategoryDTO> categoryDTOList = categoryService.getAllCategories();
        assertTrue(CollectionUtils.isEmpty(categoryDTOList));
    }

    @Test
    public void testGetCategoryByName(){
        when(categoryDBService.getCategoryByName(anyString())).thenReturn(buildList(2));
        List<CategoryDTO> categoryDTOList = categoryService.getCategoryByName("test");
        assertTrue(categoryDTOList.size() == 2);
    }

    @Test
    public void testGetCategoryByNameSizeZero(){
        when(categoryDBService.getCategoryByName(anyString())).thenReturn(buildList(0));
        List<CategoryDTO> categoryDTOList = categoryService.getCategoryByName("test");
        assertTrue(CollectionUtils.isEmpty(categoryDTOList));
    }

    private List<CategoryEntity> buildList(int size) {
        List<CategoryEntity> categoryEntities = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            categoryEntities.add(buildEntity());
        }
        return categoryEntities;
    }

    private CategoryEntity buildEntity() {
        return new CategoryEntity("ID", "name", buildUserEntity());
    }

    private UserEntity buildUserEntity() {
        return new UserEntity("user", "firstName", "lastName", "pw", UserRole.TU);
    }
}
