package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.UITexts;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("categoryBean")
@RequestScoped
public class CategoryBean extends CategoryDTO{

    private static final Logger LOG = LogManager.getLogger(CategoryBean.class);

    @Inject
    private UserService userService;
    @Inject
    private CategoryService categoryService;
    @Inject
    private CategoryRepositoryBean categoryRepositoryBean;

    private String tutorUserId;

    public void createCategory(){
        UserDTO userDTO = null;
        try {
            userDTO = userService.getUserByUserId(tutorUserId);
            CategoryDTO categoryDTO = new CategoryDTO(super.getCategoryId(), super.getCategoryName(), userDTO);
            categoryService.addCategory(categoryDTO);
            categoryRepositoryBean.addNewCategory(categoryDTO);
        } catch (UserNotExistsException e) {
            LOG.error(ExceptionUtils.getRootCause(e));
            FacesContextUtils.resolveError(UITexts.CREATE_CATEGORY_ERROR_USER, UITexts.CREATE_CATEGORY_ERROR_USER, null);
        }
    }

    public String getTutorUserId() {
        return tutorUserId;
    }

    public void setTutorUserId(String tutorUserId) {
        this.tutorUserId = tutorUserId;
    }

}
