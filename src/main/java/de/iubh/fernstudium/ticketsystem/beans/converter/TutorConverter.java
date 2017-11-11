package de.iubh.fernstudium.ticketsystem.beans.converter;

import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.apache.commons.lang3.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

@Named("tutorConverter")
public class TutorConverter implements Converter {

    @Inject
    private UserService userService;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(StringUtils.isNotEmpty(s)){
            try {
                return userService.getUserByUserId(s);
            } catch (UserNotExistsException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(o != null){
            return ((UserDTO) o).getUserId();
        }
        return null;
    }
}
