package de.iubh.fernstudium.ticketsystem.util;

import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;
import org.apache.commons.lang.RandomStringUtils;
import org.mindrot.jbcrypt.BCrypt;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by ivanj on 04.07.2017.
 */
@ApplicationScoped
public class PasswordUtilImpl implements PasswordUtil {

    public boolean authentificate(String password, String storedHash) throws InvalidPasswordException{

        if(null == storedHash || !storedHash.startsWith("$2a$")){
            throw new InvalidPasswordException("Invalid hash provided for comparison");
        }
        return BCrypt.checkpw(password, storedHash);
    }

    public String hashPw(String password) {
        int bCryptWorkFactor = 13;
        String salt = BCrypt.gensalt(bCryptWorkFactor);
        return BCrypt.hashpw(password, salt);
    }

    public String generatePassword(){
        return RandomStringUtils.random(8, true, false);
    }
}
