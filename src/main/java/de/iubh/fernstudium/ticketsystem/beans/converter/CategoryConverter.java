package de.iubh.fernstudium.ticketsystem.beans.converter;

import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import org.apache.commons.lang3.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

@Named("categoryConverter")
public class CategoryConverter implements Converter {

    @Inject
    private CategoryService categoryService;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(StringUtils.isNotEmpty(s)){
            try {
                return categoryService.getCategoryById(s);
            } catch (CategoryNotFoundException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(o != null){
            return ((CategoryDTO) o).getCategoryId();
        }
        return null;
    }
}
