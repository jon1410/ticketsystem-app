package de.iubh.fernstudium.ticketsystem.util;

import de.iubh.fernstudium.ticketsystem.domain.InvalidPasswordException;

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
    public boolean authentificate(String password, String storedHash) throws InvalidPasswordException;

    /**
     * Hashed das PW mit BCrypt
     *
     * @param password
     * @return
     */
    public String hashPw(String password);

}
