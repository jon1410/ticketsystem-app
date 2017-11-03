package de.iubh.fernstudium.ticketsystem.services.impl;

import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.UserDBService;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidCredentialsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.EmailService;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);

    @Inject
    private UserDBService userDBService;

    @Inject
    private PasswordUtil passwordUtil;

    @Inject
    private EmailService emailService;

    @Override
    public UserDTO getUserByUserId(String userId) throws UserNotExistsException {
        UserEntity userEntity = userDBService.findById(userId);
        if (userEntity == null) {
            throw new UserNotExistsException("Could not find User with UserID: " + userId);
        }
        return userEntity.toDto();
    }

    @Override
    public boolean createUser(String userId, String firstName, String lastName, String password, UserRole role) throws UserAlreadyExistsException {
        if (this.userIdExists(userId)) {
            throw new UserAlreadyExistsException("User with UserId: " + userId + "already exists");
        }

        String hasedPw = passwordUtil.hashPw(password);
        UserEntity userEntity = new UserEntity(userId, firstName, lastName, hasedPw, role);

        try {
            userDBService.persistUser(userEntity);
        } catch (Exception ex) {
            LOG.error(ExceptionUtils.getRootCause(ex));
            return false;
        }
        return true;
    }

    @Override
    public UserDTO login(String userId, String password) throws UserNotExistsException, InvalidPasswordException, InvalidCredentialsException {
        UserDTO user = this.getUserByUserId(userId);
        if (!passwordUtil.authentificate(password, user.getPassword())) {
            throw new InvalidCredentialsException("Ungültige Anmeldedaten!");
        }
        return user;
    }

    @Override
    public boolean changePassword(String userId, String altesPw, String neuesPw) throws UserNotExistsException, InvalidPasswordException {
        UserDTO user = this.getUserByUserId(userId);

        //first Plain Check, maybe PW is already encoded, then Ecnryption Algo
        if (altesPw.equals(user.getPassword()) || passwordUtil.authentificate(altesPw, user.getPassword())) {
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

    @Override
    public boolean changeUserData(String userId, String firstName, String lastName, UserRole newRole) {
        try {
            userDBService.updateUser(userId, firstName, lastName, newRole);
        } catch (Exception ex) {
            LOG.error(ExceptionUtils.getRootCause(ex));
            return false;
        }
        return true;
    }

    @Override
    public List<UserDTO> getAllTutors() {
        List<UserEntity> users = userDBService.findByRole(UserRole.TU);
        return convertToDtoList(users);
    }

    @Override
    public void generateNewPassword(String mailAdress) throws UserNotExistsException {
        String newPw = passwordUtil.generatePassword();
        //check if User exists...
        UserDTO user = this.getUserByUserId(mailAdress);

        if(!emailService.isMailConfigured()){
            System.out.println("### Neues Passwort ist: " + newPw);
        }

        String hashedPwNew = passwordUtil.hashPw(newPw);
        userDBService.changePassword(mailAdress, hashedPwNew);
    }

    private List<UserDTO> convertToDtoList(List<UserEntity> users) {
        if(CollectionUtils.isNotEmpty(users)){
            return users.stream().map(UserEntity::toDto).collect(Collectors.toList());
        }
        else{
            return new ArrayList<>();
        }
    }
}
