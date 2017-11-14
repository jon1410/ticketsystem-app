package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.UITexts;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import de.iubh.fernstudium.ticketsystem.util.config.ValidationConfig;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Pattern;

@Named("userBean")
@RequestScoped
public class UserBean extends UserDTO{

    private static final Logger LOG = LogManager.getLogger(UserBean.class);
    private String newPassword;
    private String repeatedPassword;

    @Pattern(regexp = ValidationConfig.EMAIL_REGEX)
    private String mailAdressForNewPw;

    @Inject
    private UserService userService;

    @Inject
    private TutorRepositoryBean tutorRepositoryBean;

    @Override
    public String getRole() {
        //notwendig für main.xhtml selectOne, sonst NPE
        if(role == null){
            return UserRole.ST.getResolvedRoleText();
        }
        return role.getResolvedRoleText();
    }

    public String getMailAdressForNewPw() {
        return mailAdressForNewPw;
    }

    public void setMailAdressForNewPw(String mailAdressForNewPw) {
        this.mailAdressForNewPw = mailAdressForNewPw;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String createUser(){
        return createNewUser(FacesContextUtils.REDIRECT_MAIN);
    }

    public String askForNewPassword(){
        try {
            userService.generateNewPassword(mailAdressForNewPw);
        } catch (UserNotExistsException e) {
           LOG.error(ExceptionUtils.getRootCauseMessage(e));
            return FacesContextUtils.resolveError(UITexts.PW_RESET_ERROR_SUMMARY,
                    UITexts.PW_RESET_ERROR_DETAILS, FacesContextUtils.REDIRECT_LOGIN);
        }
        return FacesContextUtils.resolveInfo(UITexts.PW_RESET_INFO_SUMMARY,
                UITexts.PW_RESET_INFO_DETAILS, FacesContextUtils.REDIRECT_LOGIN);
    }

    public String registerUser(){
        super.setUserRole(UserRole.ST);
        if(newPassword.equals(repeatedPassword)){
            super.setPassword(newPassword);
        }else{
            return FacesContextUtils.resolveError(UITexts.CREATE_USER_ERROR_SUMMARY,
                    UITexts.CREATE_USER_ERROR_DETAIL + UITexts.REG_PW_ERROR_DETAIL, null);
        }
        return createNewUser(FacesContextUtils.REDIRECT_LOGIN);
    }

    private String createNewUser(String redirect) {
        try {
            boolean createUser = userService.createUser(super.getUserId(),
                    super.getFirstName(), super.getLastName(), super.getPassword(), super.getUserRole());
            if(createUser){
                if(super.getUserRole() == UserRole.TU){
                    tutorRepositoryBean.setTutor(this);
                }
                return FacesContextUtils.resolveInfo(UITexts.CREATE_USER_INFO_SUMMARY, UITexts.CREATE_USER_INFO_DETAIL, redirect);
            }else{
                return errorCreateUser(redirect);
            }
        } catch (UserAlreadyExistsException e) {
            return errorCreateUser(redirect);
        }
    }

    private String errorCreateUser(String redirect) {
        return FacesContextUtils.resolveError(UITexts.CREATE_USER_ERROR_SUMMARY,
                UITexts.CREATE_USER_ERROR_DETAIL, redirect);
    }
}

