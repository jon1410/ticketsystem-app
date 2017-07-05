package de.iubh.fernstudium.ticketsystem.mocks.test;

import de.iubh.fernstudium.ticketsystem.domain.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.services.mockups.UserServiceMockup;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtil;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtilImpl;
import org.junit.Rule;
import org.junit.Test;
import org.needle4j.annotation.InjectIntoMany;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

/**
 * Created by ivanj on 05.07.2017.
 */
public class UserServiceMockupTest {

    @Rule
    public final NeedleRule needleRule = new NeedleRule();

    @ObjectUnderTest(postConstruct = true)
    private UserServiceMockup userServiceMockup;

    @InjectIntoMany
    private PasswordUtil passwordUtil = new PasswordUtilImpl();

    @Test(expected = UserAlreadyExistsException.class)
    public void adminUserExistsTest() throws UserAlreadyExistsException {

        userServiceMockup.createUser("admin",
                "admin", "admin",
                "admin", "AD", "admin@ticketsystem.de");
    }

}
