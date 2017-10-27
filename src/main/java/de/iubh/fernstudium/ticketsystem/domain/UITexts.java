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
    public static final String NEW_TICKET_SUMMARY = "Neues Ticket erstellt";

    //Passwort neu Texte
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


    //Sonst
    public static final String CREATE_USER_TRY_AGAIN = "bitte probiere es später noch einmal";
}
