package de.iubh.fernstudium.ticketsystem.services;

public interface EmailService {

    boolean sendEmail();

    boolean isMailConfigured();
}
