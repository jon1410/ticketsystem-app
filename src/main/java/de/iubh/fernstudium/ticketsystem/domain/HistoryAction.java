package de.iubh.fernstudium.ticketsystem.domain;

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
}
