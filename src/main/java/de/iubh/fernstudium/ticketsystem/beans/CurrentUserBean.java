package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.event.payload.CachePayload;
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

    public void init(UserDTO userDTO){
        LOG.info("Initilizing Current User...: " + userDTO.toString());
        super.setPassword(userDTO.getPassword());
        super.setFirstName(userDTO.getFirstName());
        super.setLastName(userDTO.getLastName());
        super.setUserRole(userDTO.getUserRole());
        super.setUserId(userDTO.getUserId());
        resolvedUserRole = userDTO.getUserRole().getResolvedRoleText();
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

    public String changeUserData(){

        //nothin to do here
        if(getNewFirstName().equals(getFirstName()) && getNewLastName().equals(getLastName())){
            restoreNewValuesToNull();
            return FacesContextUtils.resolveInfo("Daten sind gleich, daher wurden sie nicht geändert",
                    "Gleiche Daten eingegeben",  "main.xhtml?faces-redirect=true");
        }

        userService.changeUserData(super.getUserId(), getNewFirstName(), getNewLastName(), super.getUserRole());
        super.setFirstName(getNewFirstName());
        super.setLastName(getNewLastName());

        UserDTO cacheUpdate = createUserDto();
        eventProducer.produceCacheEvent(new CachePayload(cacheUpdate));
        restoreNewValuesToNull();
        return null;
    }

    public UserDTO createUserDto() {
        return new UserDTO(super.getUserId(), super.getFirstName(), super.getLastName(), super.getPassword(), super.getUserRole());
    }

    private void restoreNewValuesToNull() {
        this.newLastName = null;
        this.newFirstName = null;
    }

    public String logout(){
        return FacesContextUtils.logout(FacesContextUtils.REDIRECT_LOGIN);
    }
}
