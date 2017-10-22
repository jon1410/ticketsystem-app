package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named("tutorRepositroyBean")
@ApplicationScoped
public class TutorRepositroyBean {

    private static final Logger LOG = LogManager.getLogger(TutorRepositroyBean.class);

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
}
