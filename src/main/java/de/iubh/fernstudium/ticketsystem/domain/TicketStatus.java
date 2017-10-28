package de.iubh.fernstudium.ticketsystem.domain;

/**
 * Created by ivanj on 03.07.2017.
 */
public enum TicketStatus {

    NEW("neu"),
    IPU("in Bearbeitung durch IUBH"),
    IPS("in Bearbeitung durch Student"),
    RES("gelöst"),
    CLO("geschlossen"),
    DEL("gelöscht"),
    DLS("automatisch durch System gelöscht"),
    UST("abgebrochen durch Einmelder");

    private String resolvedText;

    TicketStatus(String neu) {
        this.resolvedText = neu;
    }

    public String getResolvedText(){
        return resolvedText;
    }
}
