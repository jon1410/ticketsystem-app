package de.iubh.fernstudium.ticketsystem.services.mockups;

import de.iubh.fernstudium.ticketsystem.domain.InvalidPasswordException;
import de.iubh.fernstudium.ticketsystem.domain.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.domain.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivanj on 04.07.2017.
 */
public class UserServiceMockup implements UserService {

    private Map<String, UserDTO> users;

    @PostConstruct
    private void initUser() {
        if (users == null) {
            users = new HashMap<>();
        }
        users.put("admin", createUserDTO("admin", "admin", "admin", "admin", "AD", "admin@ticketsystem.de"));
    }

    @Override
    public boolean createUser(String userId, String firstName, String lastName, String password, String role, String mailAdress) throws UserAlreadyExistsException {
        if(!users.containsKey(userId)){
            throw new UserAlreadyExistsException(String.format("User mit UserID: %s existiert bereits", userId));
        }
        users.put(userId, createUserDTO(userId, firstName, lastName, password, role, mailAdress));
        return true;
    }

    @Override
    public boolean login(String userId, String password) throws UserNotExistsException, InvalidPasswordException {
        if(!users.containsKey(userId)){
            throw new UserNotExistsException(String.format("User mit UserID: %s existiert nicht", userId));
        }

        UserDTO user = users.get(userId);
        return false;
    }

    @Override
    public boolean changePassword(String userName, String altesPw, String neuesPw) {
        return false;
    }

    private UserDTO createUserDTO(String admin, String firstName, String lastName, String passwort, String role, String mailAdress) {
        return new UserDTO(admin, firstName, lastName, passwort, UserRole.valueOf(role), mailAdress);
    }
}
