package de.iubh.fernstudium.ticketsystem.util;

import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;

/**
 * Created by ivanj on 04.07.2017.
 */
public interface PasswordUtil {

    /**
     * Validates the provided PW with the stored Hash
     *
     * @param password
     * @param storedHash
     * @return boolean
     * @throws InvalidPasswordException
     */
    boolean authentificate(String password, String storedHash) throws InvalidPasswordException;

    /**
     * Hashed das PW mit BCrypt
     *
     * @param password
     * @return
     */
    String hashPw(String password);

    /**
     * Generiert ein neues Passwort für den User
     *
     * @return neues Passwort
     */
    String generatePassword();

}
