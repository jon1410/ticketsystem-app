package de.iubh.fernstudium.ticketsystem.services;

import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidCredentialsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;

import java.util.List;

/**
 * Created by ivanj on 03.07.2017.
 */
public interface UserService {

    /**
     * Ermittelt die Userdaten zu einer UserID
     *
     * @param userId
     * @return UserDTO
     * @throws UserNotExistsException
     */
    UserDTO getUserByUserId(String userId) throws UserNotExistsException;

    /**
     * Erzeugt einen neuen User im System.
     * Passwort muss gehasht übertragen werden.
     *
     * @param userId
     * @param firstName
     * @param lastName
     * @param password
     * @param role
     * @throws UserAlreadyExistsException
     * @return boolean, wenn User korrekt erzeugt werden konnte
     */
    boolean createUser(String userId, String firstName, String lastName, String password, UserRole role) throws UserAlreadyExistsException;

    /**
     * Login des Users mit seinen Credentials.
     *
     * @param userId
     * @param password
     * @throws InvalidPasswordException
     * @throws  UserNotExistsException
     * @throws InvalidCredentialsException
     * @return boolean, wenn Credentials korrekt
     */
    UserDTO login(String userId, String password) throws UserNotExistsException, InvalidPasswordException, InvalidCredentialsException;

    /**
     * Ändert das Passwort des User. Hierfür muss das alte Passwort mitgeschickt werden.
     * Dieses wird explizit nochmal geprüft, bevor das neue Passwort gesetzt wird.
     *
     * @param userId
     * @param altesPw
     * @param neuesPw
     * @throws UserNotExistsException
     * @return boolean, wenn PW erfolgreich geändert wurde
     */
    boolean changePassword(String userId, String altesPw, String neuesPw) throws UserNotExistsException, InvalidPasswordException;

    /**
     * Überprüft, ob gewünschte UserId bereits vergeben ist.
     *
     * @param userId
     * @return true, wenn UserId bereits vergeben, sonst false
     */
    boolean userIdExists(String userId);

    /**
     * Ändert Vor- und/oder Nachnamen eines Users, sowie möglicherweise seine Rolle
     *
     * @param userId
     * @param firstName
     * @param lastName
     * @param newRole
     * @return true, wenn Daten geändert werden konnten
     */
    boolean changeUserData(String userId, String firstName, String lastName, UserRole newRole);

    /**
     * Lädt alle User mit der Rolle "Tutor" im Datenbestand
     *
     * @return List an UserDTOs
     */
    List<UserDTO> getAllTutors();

    /**
     * Generiert ein neues Passwort und verschickt dieses per Email an den User
     *
     * @param mailAdress
     * @throws UserNotExistsException
     */
    void generateNewPassword(String mailAdress) throws UserNotExistsException;
}
