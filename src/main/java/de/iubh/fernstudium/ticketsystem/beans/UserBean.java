package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("userBean")
@RequestScoped
public class UserBean extends UserDTO{

    private static final Logger LOG = LogManager.getLogger(UserBean.class);
    private String repeatedPassword;

    @Inject
    private UserService userService;

    @Inject
    private CurrentUserBean currentUser;

    @Override
    public String getRole() {
        //notwendig für main.xhtml selectOne, sonst NPE
        if(role == null){
            return UserRole.ST.getResolvedRoleText();
        }
        return role.getResolvedRoleText();
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }

    public String createUser(){
        return createNewUser("main.xhtml?faces-redirect=true", "Fehler beim Erstellen eines neuen Users");
    }

    public String registerUser(){
        return createNewUser("login.xhtml?faces-redirect=true", "Neuer User konnte nicht erstellt werden");
    }

    public String changePassword(){
        try {
            boolean isPwChanged = userService.changePassword(super.getUserId(), super.getPassword(), this.getRepeatedPassword());
            if(isPwChanged){
                return  FacesContextUtils.resolveInfo("Passwort erfolgreich geändert",
                        "Sie haben das Passwort erfolgreich geändert", "main.xhtml");
            }else{
              return resolveChangePasswordError();
            }
        } catch (UserNotExistsException | InvalidPasswordException e ) {
            return resolveChangePasswordError();
        }
    }

    public String changeUserData(){
        if(currentUser.toEntity().equals(this.toEntity())){
            return FacesContextUtils.resolveInfo("Keine Datenänderung festgestellt.",
                    "Die Daten sind gleich, keine Änderungen durchgeführt", "main.xhtml");
        }
        if(!userService.changeUserData(super.getUserId(), super.getFirstName(), super.getLastName(), super.getUserRole())){
            return FacesContextUtils.resolveError("Fehler beim Ändern der Benutzerdaten",
                    "Die Daten konnten nicht geändert werden.", null);
        };
        return "main.xhtml";
    }

    private String resolveChangePasswordError() {
        return FacesContextUtils.resolveError("Fehler bei Passwortänderung",
                "Das Passwort konnte nicht geändert werden", null);
    }

    private String createNewUser(String redirect, String errorText) {
        try {
            boolean createUser = userService.createUser(super.getUserId(),
                    super.getFirstName(), super.getLastName(), super.getPassword(), super.getUserRole());
            if(createUser){
                return redirect;
            }else{
                return FacesContextUtils.resolveError(errorText,
                        "blablabla", null);
            }
        } catch (UserAlreadyExistsException e) {
            return FacesContextUtils.resolveError(errorText,
                    "blablabla", null);
        }
    }
}

