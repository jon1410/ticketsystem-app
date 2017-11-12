package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.UITexts;
import de.iubh.fernstudium.ticketsystem.domain.event.payload.CachePayload;
import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import de.iubh.fernstudium.ticketsystem.services.impl.EventProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Dieses Bean führt ausschließlich Änderungen für den aktuellen Benutzer durch
 *
 */
@Named("currentUserBean")
@SessionScoped
public class CurrentUserBean extends UserDTO implements Serializable {

    @Inject
    private UserService userService;
    @EJB
    private EventProducer eventProducer;

    private static final Logger LOG = LogManager.getLogger(CurrentUserBean.class);
    private String resolvedUserRole;
    private String newFirstName;
    private String newLastName;
    private String newPassword;
    private String repeatedPassword;

    public void init(UserDTO userDTO){
        LOG.info("Initilizing Current User...: " + userDTO.toString());
        super.setPassword(userDTO.getPassword());
        super.setFirstName(userDTO.getFirstName());
        super.setLastName(userDTO.getLastName());
        super.setUserRole(userDTO.getUserRole());
        super.setUserId(userDTO.getUserId());
        resolvedUserRole = userDTO.getUserRole().getResolvedRoleText();
    }

    public void changeUserData(){

        //wenn alles gleich, keine Änderung durchführen
        if(getNewFirstName().equals(getFirstName()) && getNewLastName().equals(getLastName())){
            restoreNewValuesToNull();
            FacesContextUtils.resolveInfo("Daten sind gleich, daher wurden sie nicht geändert",
                    "Gleiche Daten eingegeben",  null);
        }

        userService.changeUserData(super.getUserId(), getNewFirstName(), getNewLastName(), super.getUserRole());
        super.setFirstName(getNewFirstName());
        super.setLastName(getNewLastName());

        UserDTO cacheUpdate = createUserDto();
        eventProducer.produceCacheEvent(new CachePayload(cacheUpdate));
        restoreNewValuesToNull();
    }

    public void changePassword() {
        if (!newPassword.equals(getRepeatedPassword())) {
            FacesContextUtils.resolveError(UITexts.PW_NOT_EQUAL,
                    UITexts.PW_NOT_EQUAL, null);
        } else {
            try {
                boolean isPwChanged = userService.changePassword(super.getUserId(), super.getPassword(), this.getRepeatedPassword());
                if (isPwChanged) {
                    restoreNewValuesToNull();
                    FacesContextUtils.resolveInfo(UITexts.CHANGE_PW_OK,
                            UITexts.CHANGE_PW_OK, null);
                } else {
                    resolveChangePasswordError();
                }
            } catch (UserNotExistsException | InvalidPasswordException e) {
                resolveChangePasswordError();
            }
        }
    }

    public UserDTO createUserDto() {
        return new UserDTO(super.getUserId(), super.getFirstName(), super.getLastName(), super.getPassword(), super.getUserRole());
    }

    private void restoreNewValuesToNull() {
        this.newLastName = null;
        this.newFirstName = null;
        this.newPassword = null;
        this.repeatedPassword = null;
    }

    public String logout(){
        return FacesContextUtils.logout(FacesContextUtils.REDIRECT_LOGIN);
    }

    private void resolveChangePasswordError() {
        restoreNewValuesToNull();
        FacesContextUtils.resolveError(UITexts.CHANGE_PW_ERROR,
                UITexts.CHANGE_PW_ERROR, null);
    }

    public String getResolvedUserRole() {
        return resolvedUserRole;
    }

    public String getNewFirstName() {
        if(newFirstName == null){
            return super.getFirstName();
        }
        return newFirstName;
    }

    public void setNewFirstName(String newFirstName) {
        this.newFirstName = newFirstName;
    }

    public String getNewLastName() {
        if(newLastName == null){
            return super.getLastName();
        }
        return newLastName;
    }

    public void setNewLastName(String newLastName) {
        this.newLastName = newLastName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CurrentUserBean that = (CurrentUserBean) o;

        if (resolvedUserRole != null ? !resolvedUserRole.equals(that.resolvedUserRole) : that.resolvedUserRole != null)
            return false;
        if (newFirstName != null ? !newFirstName.equals(that.newFirstName) : that.newFirstName != null) return false;
        if (newLastName != null ? !newLastName.equals(that.newLastName) : that.newLastName != null) return false;
        if (newPassword != null ? !newPassword.equals(that.newPassword) : that.newPassword != null) return false;
        return repeatedPassword != null ? repeatedPassword.equals(that.repeatedPassword) : that.repeatedPassword == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (resolvedUserRole != null ? resolvedUserRole.hashCode() : 0);
        result = 31 * result + (newFirstName != null ? newFirstName.hashCode() : 0);
        result = 31 * result + (newLastName != null ? newLastName.hashCode() : 0);
        result = 31 * result + (newPassword != null ? newPassword.hashCode() : 0);
        result = 31 * result + (repeatedPassword != null ? repeatedPassword.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CurrentUserBean{" +
                "userService=" + userService +
                ", eventProducer=" + eventProducer +
                ", resolvedUserRole='" + resolvedUserRole + '\'' +
                ", newFirstName='" + newFirstName + '\'' +
                ", newLastName='" + newLastName + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", repeatedPassword='" + repeatedPassword + '\'' +
                '}';
    }
}
