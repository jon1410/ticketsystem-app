package de.iubh.fernstudium.ticketsystem.services.impl;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;
import de.iubh.fernstudium.ticketsystem.db.services.CategoryDBService;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.apache.commons.collections.CollectionUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CategoryServiceImpl implements CategoryService {

    @Inject
    private CategoryDBService categoryDBService;

    @Inject
    UserService userService;

    @Override
    public CategoryDTO getCategoryById(String courseId) throws CategoryNotFoundException {
        CategoryEntity c = categoryDBService.getCategoryById(courseId);
        return c.toDto();
    }

    @Override
    public boolean changeTutor(String courseId, String newTutor) throws CategoryNotFoundException, UserNotExistsException {
        UserDTO userDTO = userService.getUserByUserId(newTutor);
        return categoryDBService.changeTutor(courseId,userDTO.toEntity());
    }

    @Override
    public boolean changeCategoryName(String courseId, String newCourseName) throws CategoryNotFoundException {
        return categoryDBService.changeCategoryName(courseId, newCourseName);
    }

    @Override
    public boolean changeCategory(CategoryDTO categoryDTOs) throws CategoryNotFoundException, UserNotExistsException {
        UserDTO userDTO = userService.getUserByUserId(categoryDTOs.getTutor().getUserId());
        categoryDTOs.setTutor(userDTO);
        return categoryDBService.changeCategory(categoryDTOs.toEntity());
    }

    @Override
    public boolean deleteCategoryById(String courseId) throws CategoryNotFoundException {
        return categoryDBService.deleteCategory(courseId);
    }

    @Override
    public boolean addCategory(CategoryDTO categoryDTO) {
        return categoryDBService.addCategory(categoryDTO.toEntity());
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> categoryEntities = categoryDBService.getAllCategories();
        if(CollectionUtils.isNotEmpty(categoryEntities)){
            return categoryEntities.stream().map(CategoryEntity::toDto).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
