package de.iubh.fernstudium.ticketsystem.beans.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Utilities für den FacesContext, sowie Redirect-Config
 */
public class FacesContextUtils {

    public static final String REDIRECT_LOGIN = "/login.xhtml?faces-redirect=true";
    public static final String REDIRECT_MAIN  = "/main.xhtml?faces-redirect=true";

    public static String logout(String redirect){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        if(redirect == null){
            redirect = REDIRECT_LOGIN;
        }
        return redirect;
    }

    public static String redirectWithInformationToMain(String summary, String information){
        return resolveInfo(summary, information, REDIRECT_MAIN);
    }

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
