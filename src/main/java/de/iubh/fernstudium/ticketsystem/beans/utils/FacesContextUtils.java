package de.iubh.fernstudium.ticketsystem.beans.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FacesContextUtils {

    public static String resolveError(String summary, String detail, String returnText){
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        summary, detail));
        return returnText;
    }
}
