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

    public void createCategory() throws UserNotExistsException {
        UserDTO userDTO = userService.getUserByUserId(tutorUserId);
        CategoryDTO categoryDTO = new CategoryDTO(super.getCategoryId(), super.getCategoryName(), userDTO);
        categoryService.addCategory(categoryDTO);
        categoryRepositoryBean.addNewCategory(categoryDTO);
    }

    public String getTutorUserId() {
        return tutorUserId;
    }

    public void setTutorUserId(String tutorUserId) {
        this.tutorUserId = tutorUserId;
    }

}
