package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("loginBean")
@RequestScoped
public class LoginBean {

    private static final Logger LOG = LogManager.getLogger(LoginBean.class);

    @Inject
    private UserService userService;

    private String userEmail;
    private String userPasswort;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPasswort() {
        return userPasswort;
    }

    public void setUserPasswort(String userPasswort) {
        this.userPasswort = userPasswort;
    }

    public String checkLogin(){

        try {
            if(userService.login(userEmail, userPasswort)){
                return "main.xhtml?faces-redirect=true";
            }else{
                return FacesContextUtils.resolveError("Anmeldefehler", "EMail/-Passwortkombination wurde nicht gefunden", null);
            }
        } catch (UserNotExistsException | InvalidPasswordException e) {
            LOG.error(ExceptionUtils.getRootCauseMessage(e));
            return FacesContextUtils.resolveError("Anmeldefehler", "EMail/-Passwortkombination wurde nicht gefunden", null);
        }
    }

}
