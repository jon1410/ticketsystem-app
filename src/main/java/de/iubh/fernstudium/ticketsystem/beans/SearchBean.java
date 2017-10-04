package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.services.SearchService;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@SessionScoped
@Named("searchBean")
public class SearchBean implements Serializable {

    @EJB
    private SearchService searchService;

    private boolean checkedTicketid;
    private boolean checkedDatum;
    private boolean checkedReporter;
    private boolean checkedStatus;

    private String searchString;
    private List<TicketDTO> foundTickets;

    @PostConstruct
    public void init(){
        checkedDatum = true;
        checkedTicketid = true;
        checkedStatus = true;
        checkedReporter = true;
    }

    public void search(){

    }

    public void searchDetails(){

    }

    public boolean isCheckedTicketid() {
        return checkedTicketid;
    }

    public void setCheckedTicketid(boolean checkedTicketid) {
        this.checkedTicketid = checkedTicketid;
    }

    public boolean isCheckedDatum() {
        return checkedDatum;
    }

    public void setCheckedDatum(boolean checkedDatum) {
        this.checkedDatum = checkedDatum;
    }

    public boolean isCheckedReporter() {
        return checkedReporter;
    }

    public void setCheckedReporter(boolean checkedReporter) {
        this.checkedReporter = checkedReporter;
    }

    public boolean isCheckedStatus() {
        return checkedStatus;
    }

    public void setCheckedStatus(boolean checkedStatus) {
        this.checkedStatus = checkedStatus;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
