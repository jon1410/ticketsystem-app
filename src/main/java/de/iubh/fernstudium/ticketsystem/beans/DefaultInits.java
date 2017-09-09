package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DefaultInits {
    private static final Logger LOG = LogManager.getLogger(DefaultInits.class);

    @Inject
    private UserService userService;

    private UserDTO defaultAssginee;

    @PostConstruct
    public void init(){
        try {
            defaultAssginee = userService.getUserByUserId("tutor@iubh.de");
        } catch (UserNotExistsException e) {
            LOG.error("Could not initilize Default Assginee... ");
            LOG.error(ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public UserDTO getDefaultAssginee() {
        return defaultAssginee;
    }
}
