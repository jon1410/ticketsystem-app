package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.UITexts;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@ApplicationScoped
@Named("categoryRepositoryBean")
public class CategoryRepositoryBean {

    private static final Logger LOG = LogManager.getLogger(CategoryRepositoryBean.class);

    private List<CategoryDTO> allCategories;

    @Inject
    private CategoryService categoryService;

    @PostConstruct
    public void initCategories(){
        allCategories = categoryService.getAllCategories();
    }

    public List<CategoryDTO> getAllCategories() {
        return allCategories;
    }

    public void deleteCategory(CategoryDTO categoryDTO){
        try {
            if(categoryDTO == null){
                throw new CategoryNotFoundException("DTO ist null!");
            }
            categoryService.deleteCategoryById(categoryDTO.getCategoryId());
        } catch (CategoryNotFoundException e) {
            FacesContextUtils.resolveError(UITexts.DELETE_CATEGORY_ERROR_SUMMARY, UITexts.DELETE_CATEGORY_ERROR_DETAIL, null);
            return;
        }
        allCategories.remove(categoryDTO);
    }

    /**
     * Ändert Beschreibung oder Verantwortlichkeit (Tutor) einer Kategorie
     *
     * @param categoryDTO
     */
    public void changeCategory(CategoryDTO categoryDTO){
        try {
            CategoryDTO dto = categoryService.changeCategory(categoryDTO);

            //wenn nicht null, dann gab es eine Änderung
            if(dto != null){
                updateCache(dto);
            }
        } catch (CategoryNotFoundException | UserNotExistsException e) {
            LOG.error(ExceptionUtils.getRootCauseMessage(e));
            FacesContextUtils.resolveError(UITexts.CHANGE_CATEGORY_ERROR, UITexts.CHANGE_CATEGORY_ERROR, null);
        }
    }

    public void addNewCategory(CategoryDTO categoryDTO){
        allCategories.add(categoryDTO);
    }

    public void updateCache(UserDTO userDTO) {
        for(CategoryDTO c : allCategories){
            if(c.getTutor().getUserId().equals(userDTO.getUserId())){
                c.setTutor(userDTO);
            }
        }
    }

    public void updateCache(CategoryDTO categoryDTO) {
        for(int i=0; i<allCategories.size(); i++){
            CategoryDTO c = allCategories.get(i);
            if(c.getCategoryId().equals(categoryDTO.getCategoryId())){
                allCategories.set(i, categoryDTO);
            }
        }
    }
}
