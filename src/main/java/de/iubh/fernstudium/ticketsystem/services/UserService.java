package de.iubh.fernstudium.ticketsystem.services;

import de.iubh.fernstudium.ticketsystem.domain.InvalidPasswordException;
import de.iubh.fernstudium.ticketsystem.domain.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.domain.UserNotExistsException;

/**
 * Created by ivanj on 03.07.2017.
 */
public interface UserService {

    /**
     * Erzeugt einen neuen User im System.
     * Passwort muss gehasht übertragen werden.
     *
     * @param userId
     * @param firstName
     * @param lastName
     * @param password
     * @param role
     * @param mailAdress
     * @throws UserAlreadyExistsException
     * @return boolean, wenn User korrekt erzeugt werden konnte
     */
    public boolean createUser(String userId, String firstName, String lastName, String password, String role, String mailAdress) throws UserAlreadyExistsException;

    /**
     * Login des Users mit seinen Credentials.
     *
     * @param userId
     * @param password
     * @throws InvalidPasswordException, {@link UserNotExistsException}
     * @return boolean, wenn Credentials korrekt
     */
    public boolean login(String userId, String password) throws UserNotExistsException, InvalidPasswordException;

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
    public boolean changePassword(String userId, String altesPw, String neuesPw) throws UserNotExistsException, InvalidPasswordException;


}
