package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import de.iubh.fernstudium.ticketsystem.services.UserService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
@Named("categoryRepositoryBean")
public class CategoryRepositoryBean {

    private List<CategoryDTO> allCategories;

    @Inject
    private CategoryService categoryService;
    @Inject
    private UserService userService;

    @PostConstruct
    public void initCategories() throws UserNotExistsException {
        allCategories = categoryService.getAllCategories();
        //TODO: IF raus, wenn fertig
        if(allCategories == null || allCategories.size() == 0) {
            allCategories = new ArrayList<>();
            UserDTO temp = userService.getUserByUserId("ivan@ticketsystem.de");
            allCategories.add(new CategoryDTO("ISEF01", "Software Engineering Fallstudie", temp));
            allCategories.add(new CategoryDTO("BWIR01", "Einführung in das wissenschaftliche Arbeiten", temp));
            allCategories.add(new CategoryDTO("BBLO01", "Beschaffung und Logistik", temp));
        }
    }

    public List<CategoryDTO> getAllCategories() {
        return allCategories;
    }

    public void addNewCategory(CategoryDTO categoryDTO){
        allCategories.add(categoryDTO);
    }
}
