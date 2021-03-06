package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Dient als Cache für alle Tutoren im System
 */
@Named("tutorRepositroyBean")
@ApplicationScoped
public class TutorRepositoryBean {

    private static final Logger LOG = LogManager.getLogger(TutorRepositoryBean.class);

    private List<UserDTO> allTutors;

    @Inject
    private UserService userService;

    @PostConstruct
    public void initializeTutors(){
        allTutors = userService.getAllTutors();
        LOG.info(String.format("Loaded %d Tutors", allTutors.size()) );
    }

    public List<UserDTO> getAllTutors() {
        return allTutors;
    }

    public void setAllTutors(List<UserDTO> allTutors) {
        this.allTutors = allTutors;
    }

    public void setTutor(UserDTO newTutor){

        if(allTutors == null){
            allTutors = new ArrayList<>();
        }
        if(newTutor != null){
            allTutors.add(newTutor);
        }
    }

    public void updateCache(UserDTO userDTO){
        int i;
        for (i=0; i < allTutors.size(); i++) {
            if(allTutors.get(i).getUserId().equals(userDTO.getUserId())){
                allTutors.set(i, userDTO);
                break;
            }
        }
    }

}
