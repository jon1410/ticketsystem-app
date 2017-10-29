package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.UITexts;
import de.iubh.fernstudium.ticketsystem.domain.event.payload.CacheUpdatePayload;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJBTransactionRolledbackException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
@Named("categoryRepositoryBean")
public class CategoryRepositoryBean {

    private static final Logger LOG = LogManager.getLogger(CategoryRepositoryBean.class);

    private List<CategoryDTO> allCategories;

    @Inject
    private CategoryService categoryService;
    @Inject
    private UserService userService;

    @PostConstruct
    public void initCategories(){
        allCategories = categoryService.getAllCategories();
    }

    public List<CategoryDTO> getAllCategories() {
        return allCategories;
    }

    public void deleteCategory(CategoryDTO categoryDTO){
        try {
            categoryService.deleteCategoryById(categoryDTO.getCategoryId());
        } catch (CategoryNotFoundException e) {
            FacesContextUtils.resolveError(UITexts.DELETE_CATEGORY_ERROR_SUMMARY, UITexts.DELETE_CATEGORY_ERROR_DETAIL, null);
        } catch (Exception ex){
            LOG.error(ExceptionUtils.getRootCauseMessage(ex));
            FacesContextUtils.resolveError(UITexts.DELETE_CATEGORY_DB_ERROR_DETAIL, UITexts.DELETE_CATEGORY_DB_ERROR_DETAIL, null);
            return;
        }
        allCategories.remove(categoryDTO);
    }

    public void changeCategory(CategoryDTO categoryDTO){

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
