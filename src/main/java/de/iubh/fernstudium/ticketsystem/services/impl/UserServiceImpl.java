package de.iubh.fernstudium.ticketsystem.services.impl;

import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.UserDBService;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);

    @Inject
    private UserDBService userDBService;

    @Inject
    private PasswordUtil passwordUtil;

    @Override
    public UserDTO getUserByUserId(String userId) throws UserNotExistsException {
        UserEntity userEntity = userDBService.findById(userId);
        if(userEntity == null){
            throw new UserNotExistsException("Could not find User with UserID: " + userId);
        }
        return userEntity.toDto();
    }

    @Override
    public boolean createUser(String userId, String firstName, String lastName, String password, UserRole role, String mailAdress) throws UserAlreadyExistsException {
        if(this.userIdExists(userId)){
            throw new UserAlreadyExistsException("User with UserId: " + userId + "already exists");
        }
        UserEntity userEntity = new UserEntity(userId,firstName,lastName,password,role,mailAdress);

        try{
            userDBService.persistUser(userEntity);
        }catch (Exception ex){
            LOG.error(ExceptionUtils.getRootCause(ex));
            return false;
        }
        userDBService.persistUser(userEntity);
        return true;
    }

    @Override
    public boolean login(String userId, String password) throws UserNotExistsException, InvalidPasswordException {
        UserDTO user = this.getUserByUserId(userId);
        return passwordUtil.authentificate(password, user.getPassword());
    }

    @Override
    public boolean changePassword(String userId, String altesPw, String neuesPw) throws UserNotExistsException, InvalidPasswordException {
        UserDTO user = this.getUserByUserId(userId);

        if(passwordUtil.authentificate(altesPw, user.getPassword())){
            String hashedPwNew = passwordUtil.hashPw(neuesPw);
            userDBService.changePassword(userId, hashedPwNew);
            return true;
        }
        return false;
    }

    @Override
    public boolean userIdExists(String userId) {
        UserEntity userEntity = userDBService.findById(userId);
        return userEntity != null;
    }
}
