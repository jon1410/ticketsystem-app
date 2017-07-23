package de.iubh.fernstudium.ticketsystem.services.mockups;

import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtil;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivanj on 04.07.2017.
 *
 * Dieser Mock ist ApplicationScoped,
 * damit die PostConstructMethode nicht ständig aufgerufen wird (simuliert einen Datenbestand).
 * Das tatsächliche Bean wird dann RequestScope.
 */
@ApplicationScoped
public class UserServiceMockup implements UserService {

    @Inject
    private PasswordUtil passwordUtil;

    private Map<String, UserDTO> users;

    @PostConstruct
    private void initUser() {
        if (users == null) {
            users = new HashMap<>();
        }
        users.put("admin", createUserDTO("admin", "admin", "admin", passwordUtil.hashPw("admin"), UserRole.AD, "admin@ticketsystem.de"));
    }

    @Override
    public UserDTO getUserByUserId(String userId) throws UserNotExistsException {
        return users.get(userId);
    }

    @Override
    public boolean createUser(String userId, String firstName, String lastName, String password, UserRole role, String mailAdress) throws UserAlreadyExistsException {
        if(users.containsKey(userId)){
            throw new UserAlreadyExistsException(String.format("User mit UserID: %s existiert bereits", userId));
        }
        String hashedPw = passwordUtil.hashPw(password);
        users.put(userId, createUserDTO(userId, firstName, lastName, hashedPw, role, mailAdress));
        return true;
    }

    @Override
    public boolean login(String userId, String password) throws UserNotExistsException, InvalidPasswordException {
        if(!users.containsKey(userId)){
            throw new UserNotExistsException(String.format("User mit UserID: %s existiert nicht", userId));
        }

        UserDTO user = users.get(userId);
        return passwordUtil.authentificate(password, user.getPassword());
    }

    @Override
    public boolean changePassword(String userId, String altesPw, String neuesPw) throws UserNotExistsException, InvalidPasswordException {

        if(!users.containsKey(userId)){
            throw new UserNotExistsException(String.format("User mit UserID: %s existiert nicht", userId));
        }
        UserDTO user = users.get(userId);

        if(passwordUtil.authentificate(altesPw, user.getPassword())){
            String hashedPwNew = passwordUtil.hashPw(neuesPw);
            user.setPassword(hashedPwNew);
            users.put(userId, user);
            return true;
        }

        return false;
    }

    private UserDTO createUserDTO(String admin, String firstName, String lastName, String passwort, UserRole role, String mailAdress) {
        return new UserDTO(admin, firstName, lastName, passwort, role, mailAdress);
    }
}
