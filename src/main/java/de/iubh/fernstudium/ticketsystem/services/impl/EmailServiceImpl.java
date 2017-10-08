package de.iubh.fernstudium.ticketsystem.services.impl;

import de.iubh.fernstudium.ticketsystem.services.EmailService;
import de.iubh.fernstudium.ticketsystem.util.config.EmailConfig;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.mail.Session;
import java.util.Properties;

@ApplicationScoped
public class EmailServiceImpl implements EmailService {

    @Resource(mappedName = EmailConfig.JNDI_MAIL)
    private Session session;

    @Override
    public boolean sendEmail() {
        return false;
    }

    @Override
    public boolean isMailConfigured() {

        if(session == null){
            return false;
        }

        Properties mailProps = session.getProperties();
        System.out.println(mailProps.toString());

        return false;
    }
}
