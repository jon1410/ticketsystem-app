package de.iubh.fernstudium.ticketsystem.services;

public interface EmailService {

    /**
     * Sendet eine Email
     *
     * @return boolean
     */
    boolean sendEmail();

    /**
     * Prüft, ob ein MailClient konfiguriert ist, indem geprüft wird
     * ob die Session aus dem angegebenen JNDI-Namen erzeugt werden kann
     * @return boolean
     */
    boolean isMailConfigured();
}
