package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.UITexts;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Dieses Bean dient als Cache für alle Kategorien und führt Änderungen an diesen durch
 */
@SessionScoped
@Named("categoryRepositoryBean")
public class CategoryRepositoryBean implements Serializable {

    private static final Logger LOG = LogManager.getLogger(CategoryRepositoryBean.class);

    private List<CategoryDTO> allCategories;
    private CategoryDTO currentDTO;
    private String newCategoryName;
    private String newTutorUserId;

    @Inject
    private CategoryService categoryService;
    @Inject
    private UserService userService;

    /**
     * {@link PostConstruct} - Methode
     * Initialisiert alle Katogrien nach Login
     */
    @PostConstruct
    public void initCategories(){
        allCategories = categoryService.getAllCategories();
    }

    /**
     * Liefert alle geladenen Kategorien der Session
     * @return Liste an {@link CategoryDTO}
     */
    public List<CategoryDTO> getAllCategories() {
        return allCategories;
    }

    /**
     * Löscht eine Kategorie
     * @param categoryDTO
     */
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
     * Ändert eine Kategorie, basierend auf dem
     * aktuell ausgewählten DTO
     */
    public void changeCategory(){
        if(currentDTO == null){
            FacesContextUtils.resolveError(UITexts.CHANGE_CATEGORY_ERROR, UITexts.CHANGE_CATEGORY_ERROR, null);
            return;
        }


        if(StringUtils.isNotEmpty(newTutorUserId)){
            try {
                UserDTO newTutor = userService.getUserByUserId(newTutorUserId);
                if(newTutor.getUserRole() == UserRole.TU){
                    currentDTO.setTutor(newTutor);
                }else{
                    FacesContextUtils.resolveError(UITexts.NOT_A_TUTOR, UITexts.NOT_A_TUTOR, null);
                    return;
                }
            } catch (UserNotExistsException e) {
                LOG.error(ExceptionUtils.getRootCauseMessage(e));
                FacesContextUtils.resolveError(UITexts.NOT_A_TUTOR, UITexts.NOT_A_TUTOR, null);
            }
        }

        if(StringUtils.isNotEmpty(newCategoryName)){
            currentDTO.setCategoryName(newCategoryName);
        }


        this.changeCategory(currentDTO);
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
        FacesContextUtils.resolveInfo(UITexts.CHANGE_EXEC, UITexts.CHANGE_EXEC, null);
    }

    /**
     * Erzeugt eine neue Kategorie
     * @param categoryDTO
     */
    public void addNewCategory(CategoryDTO categoryDTO){
        allCategories.add(categoryDTO);
    }

    /**
     * Updatet den Cache, wenn ein User mit Rolle = Tutor geändert wurde
     * @param userDTO
     */
    public void updateCache(UserDTO userDTO) {
        for(CategoryDTO c : allCategories){
            if(c.getTutor().getUserId().equals(userDTO.getUserId())){
                c.setTutor(userDTO);
            }
        }
    }

    /**
     * Update des Caches, wenn eine Kategorie geändert wurde
     * @param categoryDTO
     */
    public void updateCache(CategoryDTO categoryDTO) {
        for(int i=0; i<allCategories.size(); i++){
            CategoryDTO c = allCategories.get(i);
            if(c.getCategoryId().equals(categoryDTO.getCategoryId())){
                allCategories.set(i, categoryDTO);
            }
        }
    }

    public String getNewCategoryName() {
        return newCategoryName;
    }

    public void setNewCategoryName(String newCategoryName) {
        this.newCategoryName = newCategoryName;
    }

    public String getNewTutorUserId() {
        return newTutorUserId;
    }

    public void setNewTutorUserId(String newTutorUserId) {
        this.newTutorUserId = newTutorUserId;
    }

    public CategoryDTO getCurrentDTO() {
        return currentDTO;
    }

    public void setCurrentDTO(CategoryDTO currentDTO) {
        this.currentDTO = currentDTO;
        this.newCategoryName = currentDTO.getCategoryName();
        this.newTutorUserId = currentDTO.getTutor().getUserId();
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("$('.katChgModal').modal('show');");
    }
}
