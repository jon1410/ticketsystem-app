package de.iubh.fernstudium.ticketsystem.util.test;

import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtil;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtilImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by ivanj on 04.07.2017.
 */
public class PasswordUtilTest {

    private static final Logger LOG = LogManager.getLogger(PasswordUtilTest.class);

    @Test
    public void testHashAndAuthentificateTrue() throws InvalidPasswordException {
        PasswordUtil passwordUtil = new PasswordUtilImpl();

        String password = "mySecretPassword";
        String hashedPw = passwordUtil.hashPw(password);
        LOG.info("Hashed-Password: " + hashedPw);
        assertNotNull(hashedPw);
        Assert.assertTrue(hashedPw.startsWith("$2a$"));

        Assert.assertTrue(passwordUtil.authentificate(password, hashedPw));
    }

    @Test(expected = InvalidPasswordException.class)
    public void testHashAndAuthentificateNullFail() throws InvalidPasswordException{
        PasswordUtilImpl securityUtil = new PasswordUtilImpl();

        String password = "mySecretPassword";
        String hashedPw = securityUtil.hashPw(password);
        LOG.info("Hashed-Password: " + hashedPw);
        assertNotNull(hashedPw);
        Assert.assertTrue(hashedPw.startsWith("$2a$"));

        Assert.assertTrue(securityUtil.authentificate(null, null));
    }

    @Test(expected = InvalidPasswordException.class)
    public void testHashAndAuthentificateIllegalHashFail() throws InvalidPasswordException{
        PasswordUtilImpl securityUtil = new PasswordUtilImpl();

        String password = "mySecretPassword";
        String hashedPw = securityUtil.hashPw(password);
        LOG.info("Hashed-Password: " + hashedPw);
        assertNotNull(hashedPw);
        Assert.assertTrue(hashedPw.startsWith("$2a$"));

        Assert.assertFalse(securityUtil.authentificate(password, hashedPw.substring(1)));
    }

    @Test
    public void testHashAndAuthentificateFalse() throws InvalidPasswordException {
        PasswordUtilImpl securityUtil = new PasswordUtilImpl();

        String password = "mySecretPassword";
        String hashedPw = securityUtil.hashPw(password);
        LOG.info("Hashed-Password: " + hashedPw);
        assertNotNull(hashedPw);
        Assert.assertTrue(hashedPw.startsWith("$2a$"));

        Assert.assertFalse(securityUtil.authentificate(password.toUpperCase(), hashedPw));
    }

    @Test
    public void testBase64DecodeAndHash(){
        Assert.assertEquals("admin" , new String(Base64.getDecoder().decode("YWRtaW4="), StandardCharsets.UTF_8));
    }

    @Test
    public void testGeneratePassword(){
        PasswordUtilImpl securityUtil = new PasswordUtilImpl();
        String pw1 = securityUtil.generatePassword();
        assertNotNull(pw1);
        LOG.info("PW1 " + pw1);

        String pw2 = securityUtil.generatePassword();
        assertNotNull(pw1);
        LOG.info("PW2 " + pw2);

        assertNotEquals(pw1, pw2);
    }
}
