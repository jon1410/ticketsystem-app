package de.iubh.fernstudium.ticketsystem.util;

import de.iubh.fernstudium.ticketsystem.domain.InvalidPasswordException;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by ivanj on 04.07.2017.
 */
public class PasswordUtilImpl implements PasswordUtil {

    private static int bCryptWorkFactor = 13;

    /**
     * Validates the provided PW with the stored Hash
     *
     * @param password
     * @param storedHash
     * @return boolean
     * @throws InvalidPasswordException
     */
    public boolean authentificate(String password, String storedHash) throws InvalidPasswordException{

        boolean verified = false;

        if(null == storedHash || !storedHash.startsWith("$2a$")){
            throw new InvalidPasswordException("Invalid hash provided for comparison");
        }
        verified = BCrypt.checkpw(password, storedHash);

        return verified;
    };


    /**
     * Hashed das PW mit BCrypt
     *
     * @param password
     * @return
     */
    public String hashPw(String password) {
        String salt = BCrypt.gensalt(13);
        return BCrypt.hashpw(password, salt);
    }
}
