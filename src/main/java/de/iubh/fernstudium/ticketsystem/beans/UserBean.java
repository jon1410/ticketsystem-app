package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("userBean")
@RequestScoped
public class UserBean extends UserDTO{

    private static final Logger LOG = LogManager.getLogger(UserBean.class);

    @Inject
    private UserService userService;

    public String createUser(){
        try {
            boolean createUser = userService.createUser(super.getUserId(),
                    super.getFirstName(), super.getLastName(), super.getPassword(), super.getUserRole());
            if(createUser){
                return "...";
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
