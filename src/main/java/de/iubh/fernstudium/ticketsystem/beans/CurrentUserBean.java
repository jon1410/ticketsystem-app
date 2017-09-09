package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("currentUserBean")
@SessionScoped
public class CurrentUserBean extends UserDTO implements Serializable {

    private static final Logger LOG = LogManager.getLogger(CurrentUserBean.class);

    public void init(UserDTO userDTO){
        LOG.info("Initilizing Current User...: " + userDTO.toString());
        super.setPassword(userDTO.getPassword());
        super.setFirstName(userDTO.getFirstName());
        super.setLastName(userDTO.getLastName());
        super.setUserRole(userDTO.getUserRole());
        super.setUserId(userDTO.getUserId());
    }
}
