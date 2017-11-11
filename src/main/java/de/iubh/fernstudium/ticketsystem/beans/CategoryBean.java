package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import de.iubh.fernstudium.ticketsystem.services.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("categoryBean")
@RequestScoped
public class CategoryBean extends CategoryDTO{

    @Inject
    private UserService userService;
    @Inject
    private CategoryService categoryService;
    @Inject
    private CategoryRepositoryBean categoryRepositoryBean;

    private String tutorUserId;
    private CategoryDTO selectedCategory;
    
    //Alle Tutoren in einer Liste für die Filterung in der Kategorie "NEU"
    private List<Tutoren> alleTutoren;

    public void createCategory() throws UserNotExistsException {
        UserDTO userDTO = userService.getUserByUserId(tutorUserId);
        CategoryDTO categoryDTO = new CategoryDTO(super.getCategoryId(), super.getCategoryName(), userDTO);
        categoryService.addCourse(categoryDTO);
    }

    public String getTutorUserId() {
        return tutorUserId;
    }

    public void setTutorUserId(String tutorUserId) {
        this.tutorUserId = tutorUserId;
    }

    public CategoryDTO getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(CategoryDTO selectedCategory) {
        this.selectedCategory = selectedCategory;
    }
    
    
}
