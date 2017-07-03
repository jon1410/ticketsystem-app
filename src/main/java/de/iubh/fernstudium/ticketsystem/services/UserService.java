package de.iubh.fernstudium.ticketsystem.services;

/**
 * Created by ivanj on 03.07.2017.
 */
public interface UserService {

    /**
     * Erzeugt einen neuen User im System.
     * Passwort muss gehasht übertragen werden.
     *
     * @param userName
     * @param rolle
     * @param passwort
     * @return boolean, wenn User korrekt erzeugt werden konnte
     */
    public boolean createUser(String userName, String rolle,String passwort);

    /**
     * Login des Users mit seinen Credentials.
     *
     * @param userName
     * @param password
     * @return boolean, wenn Credentials korrekt
     */
    public boolean login(String userName, String password);

    /**
     * Ändert das Passwort des User. Hierfür muss das alte Passwort mitgeschickt werden.
     * Dieses wird explizit nochmal geprüft, bevor das neue Passwort gesetzt wird.
     *
     * @param userName
     * @param altesPw
     * @param neuesPw
     * @return boolean, wenn PW erfolgreich geändert wurde
     */
    public boolean changePassword(String userName, String altesPw, String neuesPw);


}
