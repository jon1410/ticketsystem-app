package de.iubh.fernstudium.ticketsystem.beans.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FacesContextUtils {

    public static String resolveError(String summary, String detail, String returnText){
        addMessage(FacesMessage.SEVERITY_WARN, summary, detail);
        return returnText;
    }

    public static String resolveInfo(String summary, String infoDetail, String returnText){
        addMessage(FacesMessage.SEVERITY_INFO, summary, infoDetail);
        return returnText;
    }

    private static void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity,
                        summary, detail));
    }
}
