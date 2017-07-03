package de.iubh.fernstudium.ticketsystem.domain;

/**
 * Created by ivanj on 03.07.2017.
 */
public enum TicketStatus {

    NEW("neu"),
    IN_PROGRESS_IUBH("in Bearbeitung durch IUBH"),
    IN_PROGRESS_STUDENT("in Bearbeitung durch Student"),
    RESOLVED("gelöst"),
    CLOSED("geschlossen"),
    DELETED("gelöscht"),
    DELETED_SYSTEM("automatisch durch System gelöscht");

    private String resolvedText;

    TicketStatus(String neu) {
        this.resolvedText = neu;
    }

    public String getResolvedText(){
        return resolvedText;
    }
}
