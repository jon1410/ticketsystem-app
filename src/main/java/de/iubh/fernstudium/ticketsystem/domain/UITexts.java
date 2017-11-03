package de.iubh.fernstudium.ticketsystem.domain;

public class UITexts {

    //Login-Texte
    public static final String LOGIN_ERROR_SUMMARY = "Anmeldefehler";
    public static final String LOGIN_ERROR_DETAIL = "EMail/-Passwortkombination wurde nicht gefunden";
    public static final String REG_PW_ERROR_SUMMARY = "Passwortfehler";
    public static final String REG_PW_ERROR_DETAIL = "Passwörter stimmen nicht überein!";

    //User-Texte
    public static final String CREATE_USER_ERROR_SUMMARY = "Registrierungsfehler";
    public static final String CREATE_USER_ERROR_DETAIL = "Neuer Benutzer konnte nicht angelegt werden, ";
    public static final String CREATE_USER_INFO_SUMMARY = "Registrierung erfolgreich.";
    public static final String CREATE_USER_INFO_DETAIL = "Neuer Benutzer angelegt.";

    //Ticket-Texte
    public static final String NEW_TICKET = "Neues Ticket erstellt";
    public static final String CHANGE_STAT_ERROR = "Fehler bei Stautsänderung ist aufgetreten. Vorgang abgebrochen.";
    public static final String STOP_TICKET_ERROR_SUMMARY = "Bearbeitungsvorgang nicht abgebrochen.";
    public static final String STOP_TICKET_ERROR_DETAIL = "Der Bearbeitungsvorgangs des ausgewählten Tickets kann aktuell nicht abgebrochen werden.";
    public static final String NO_ACTIVE_TICKET = "Kein Ticket aktiv!";
    public static final String ERR_MASTER_TICKET = "Es ist ein schwerwiegender Fehler aufgereten, Masterticket konnte nicht erstellt werden!";
    public static final String ERR_MASTER_TICKET_NO_CHILD = "Masterticket konnte nicht erstellt werden, keine Untertickets ausgewählt!";

    //Kommentar-Texte
    public static final String ERROR_NEW_COMMENT = "Fehler! Kommentar konnte nicht hinzugefügt werden!";

    //Passwort neu Texte
    public static final String PW_NOT_EQUAL = "Die eingegebenen Passwörter stimmen nicht überein, Passwort nicht geändert.";
    public static final String CHANGE_PW_OK = "Dein Passwort wurde erfolgreich geändert.";
    public static final String CHANGE_PW_ERROR = "Fehler aufgetreten! Passwort wurde nicht geändert!";
    public static final String PW_RESET_INFO_SUMMARY = "Passwort zurückgesetzt";
    public static final String PW_RESET_INFO_DETAILS = "Dein neues Passwort wurde per Email zugeschickt.";

    public static final String PW_RESET_ERROR_SUMMARY = "Passwort konnte nicht zurückgesetzt werden";
    public static final String PW_RESET_ERROR_DETAILS = "Die angegebene E-Mail-Adresse ist dem System nicht bekannt.";

    //Suche-Texte
    public static final String SEARCH_NOT_FOUND_SUMMARY = "Keine Tickets gefunden";
    public static final String SIMPLE_SEARCH_NOT_FOUND_DETAIL = "Zu deiner Suchanfrage wurden keine Tickets gefunden, versuche bitte die Detailsuche.";

    public static final String SEARCH_SUMMARY = "Suche abgeschlossen";
    public static final String SEARCH_DETAIL = "Zu deiner Suchanfrage wurden folgende Tickets gefunden.";
    
    public static final String DETAIL_SEARCH_NOT_FOUND_DETAIL = "Zu deiner Suchanfrage wurden keine Tickets gefunden, bitte versuche andere Parameter.";

    //Kategorie-Texte
    public static final String CREATE_CATEGORY_ERROR_USER = "Fehler beim Erzeugen der Kategorie, angegebener Tutor existiert nicht.";
    public static final String CHANGE_CATEGORY_ERROR = "Fehler beim Ändern der Kategorie, bitte wende dich an den Support.";
    public static final String DELETE_CATEGORY_ERROR_SUMMARY = "Fehler beim Löschen der Kategorie";
    public static final String DELETE_CATEGORY_ERROR_DETAIL = "Die Kategorie konnte nicht gelöscht werden, bitte versuche es später noch einmal.";
    public static final String DELETE_CATEGORY_DB_ERROR_DETAIL = "Diese Kategorie wird noch verwendet und kann nicht gelöscht werden!";


    //Sonst
    public static final String CREATE_USER_TRY_AGAIN = "bitte probiere es später noch einmal";
}
