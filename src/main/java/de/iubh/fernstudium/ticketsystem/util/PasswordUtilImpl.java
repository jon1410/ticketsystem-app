package de.iubh.fernstudium.ticketsystem.util;

import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;
import org.mindrot.jbcrypt.BCrypt;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by ivanj on 04.07.2017.
 */
@ApplicationScoped
public class PasswordUtilImpl implements PasswordUtil {

    private static int bCryptWorkFactor = 13;

    public boolean authentificate(String password, String storedHash) throws InvalidPasswordException{

        if(null == storedHash || !storedHash.startsWith("$2a$")){
            throw new InvalidPasswordException("Invalid hash provided for comparison");
        }
        return BCrypt.checkpw(password, storedHash);
    }

    public String hashPw(String password) {
        String salt = BCrypt.gensalt(13);
        return BCrypt.hashpw(password, salt);
    }
}
