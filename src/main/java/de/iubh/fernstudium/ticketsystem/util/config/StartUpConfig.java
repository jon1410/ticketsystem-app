package de.iubh.fernstudium.ticketsystem.util.config;

import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.UserDBService;
import de.iubh.fernstudium.ticketsystem.db.services.impl.UserDBServiceImpl;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtil;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtilImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Singleton
@Startup
public class StartUpConfig {
    private static final Logger LOG = LogManager.getLogger(UserDBServiceImpl.class);

    @Inject
    private UserDBService userDBService;

    private static final String USERNAME = "admin@ticketsystem.de";

    @PostConstruct
    private void createAdminUser(){

        UserEntity u = userDBService.findById(USERNAME);
        if(u == null){
            LOG.info("Creating ADMIN User....");
            PasswordUtil pwUtil = new PasswordUtilImpl();
            String pw = pwUtil.hashPw(new String(Base64.getDecoder().decode("YWRtaW4="), StandardCharsets.UTF_8));
            UserEntity userEntity = new UserEntity(USERNAME, "admin", "admin", pw, UserRole.AD);
            userDBService.persistUser(userEntity);
            LOG.info("ADMIN User created....");
        }
    }
}
