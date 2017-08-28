package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("createUserBean")
@RequestScoped
public class CreateUserBean extends UserDTO{

    private static final Logger LOG = LogManager.getLogger(CreateUserBean.class);

    @Inject
    private UserService userService;

    @Override
    public String getRole() {
        //notwendig für main.xhtml selectOne, sonst NPE
        if(role == null){
            return UserRole.ST.getResolvedRoleText();
        }
        return role.getResolvedRoleText();
    }

    public String createUser(){
        try {
            boolean createUser = userService.createUser(super.getUserId(),
                    super.getFirstName(), super.getLastName(), super.getPassword(), super.getUserRole());
            if(createUser){
                return "main.xhtml?faces-redirect=true";
            }else{
                return FacesContextUtils.resolveError("Fehler beim Erstellen eines neuen Users",
                        "blablabla", null);
            }
        } catch (UserAlreadyExistsException e) {
            return FacesContextUtils.resolveError("Fehler beim Erstellen eines neuen Users",
                    "blablabla", null);
        }
    }
}

