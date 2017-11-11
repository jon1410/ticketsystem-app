package de.iubh.fernstudium.ticketsystem.util.test;

import de.iubh.fernstudium.ticketsystem.util.config.ValidationConfig;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.regex.Pattern;

public class RegexTest {

    @Test
    public void testEmailRegex(){
        Pattern mailPattern = Pattern.compile(ValidationConfig.EMAIL_REGEX);

        String email = "ivan@ticketsystem.de";
        Assert.assertTrue(email.matches(ValidationConfig.EMAIL_REGEX));
    }

    @Test
    public void testStringSplit(){
        String s = "a,b,c";
        Assert.assertTrue(s.split(",").length == 3);

    }
}
