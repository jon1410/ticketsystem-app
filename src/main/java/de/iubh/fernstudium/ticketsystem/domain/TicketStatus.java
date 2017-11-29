package de.iubh.fernstudium.ticketsystem.domain;

/**
 * Alle Ticket-Stati als Enumeration
 */
public enum TicketStatus {

    NEW("neu"),
    IPU("in Bearbeitung durch IUBH"),
    IPS("in Bearbeitung durch Student"),
    RES("gelöst"),
    RET("Lösungsvorschlag IUBH"),
    CLO("abgeschlossen"),
    DEL("gelöscht"),
    DLS("automatisch durch System gelöscht"),
    UTU("abgebrochen durch IUBH"),
    UST("abgebrochen durch Student");

    private String resolvedText;

    TicketStatus(String neu) {
        this.resolvedText = neu;
    }

    public String getResolvedText(){
        return resolvedText;
    }

    public static TicketStatus fromString(String text){
        for(TicketStatus t : TicketStatus.values()){
            if(t.resolvedText.equalsIgnoreCase(text)){
                return t;
            }
        }
        return null;
    }
}
