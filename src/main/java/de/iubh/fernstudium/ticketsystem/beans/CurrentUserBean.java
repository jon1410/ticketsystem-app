package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("currentUserBean")
@SessionScoped
public class CurrentUserBean extends UserDTO implements Serializable {

    public void init(UserDTO userDTO){
        super.setPassword(userDTO.getPassword());
        super.setFirstName(userDTO.getFirstName());
        super.setLastName(userDTO.getLastName());
        super.setUserRole(userDTO.getUserRole());
        super.setUserId(userDTO.getUserId());
    }
}
