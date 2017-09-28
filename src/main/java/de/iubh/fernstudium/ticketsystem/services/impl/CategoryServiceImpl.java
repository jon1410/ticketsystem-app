package de.iubh.fernstudium.ticketsystem.services.impl;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;
import de.iubh.fernstudium.ticketsystem.db.services.CategoryDBService;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import de.iubh.fernstudium.ticketsystem.services.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CategoryServiceImpl implements CategoryService {

    @Inject
    private CategoryDBService courseDBService;

    @Inject
    UserService userService;

    @Override
    public CategoryDTO getCourseById(String courseId) throws CategoryNotFoundException {
        CategoryEntity c = courseDBService.getCategoryById(courseId);
        return c.toDto();
    }

    @Override
    public boolean changeTutor(String courseId, String newTutor) throws CategoryNotFoundException, UserNotExistsException {
        UserDTO userDTO = userService.getUserByUserId(newTutor);
        return courseDBService.changeTutor(courseId,userDTO.toEntity());
    }

    @Override
    public boolean changeCourseName(String courseId, String newCourseName) throws CategoryNotFoundException {
        return courseDBService.changeCategoryName(courseId, newCourseName);
    }

    @Override
    public boolean deleteCourseById(String courseId) throws CategoryNotFoundException {
        return courseDBService.deleteCategory(courseId);
    }

    @Override
    public boolean addCourse(CategoryDTO categoryDTO) {
        return courseDBService.addCategory(categoryDTO.toEntity());
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return null;
    }
}
