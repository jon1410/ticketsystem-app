package de.iubh.fernstudium.ticketsystem.domain.history;

public enum HistoryAction {

    SC("Statusänderung"),
    AC("Bearbeiter geändert"),
    CR("Ticket erstellt"),
    CL("Ticket abgeschlossen"),
    CA("Kommentar hinzugefügt");

    private String resolvedText;

    private HistoryAction(String resolvedText){
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
