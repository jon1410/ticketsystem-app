package de.iubh.fernstudium.ticketsystem.domain.history;

/**
 * Enumeration von Aktionen des Users, zu denen eine Historie geschrieben werden soll
 */
public enum HistoryAction {

    SC("Statusänderung"),
    AC("Bearbeiter geändert"),
    CR("Ticket erstellt"),
    CL("Ticket abgeschlossen"),
    CA("Kommentar hinzugefügt"),
    UC("Abbruch durch Einmelder");

    private String resolvedText;

    HistoryAction(String resolvedText){
        this.resolvedText = resolvedText;
    }

    public String getResolvedText(){
        return this.resolvedText;
    }

    public static HistoryAction fromString(String text){
        for(HistoryAction h : HistoryAction.values()){
            if(h.resolvedText.equalsIgnoreCase(text)){
                return h;
            }
        }
        return null;
    }
}
