package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.UITexts;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.services.SearchService;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SessionScoped
@Named("searchBean")
public class SearchBean implements Serializable {

    private static final Logger LOG = LogManager.getLogger(SearchBean.class);

    @EJB
    private SearchService searchService;
    @Inject
    private TicketService ticketService;

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

    public String searchSimple(){

        Set<TicketDTO> ticketSet = new HashSet<>();

        //restore found tickets
        foundTickets = null;
        if(StringUtils.isNumeric(searchString)){
            try {
                TicketDTO ticket = ticketService.getTicketByID(new Long(searchString));
                ticketSet.add(ticket);
            } catch (NoSuchTicketException e) {
                return resolveSearchSimpleInfo();
            }
        } else{
            Future<List<TicketDTO>> ticketsTitle = searchService.searchByTitle(searchString);
            Future<List<TicketDTO>> ticketsDesc = searchService.searchByDescription(searchString);
            try {
                List<TicketDTO> templist = ticketsDesc.get(3, TimeUnit.SECONDS);
                LOG.info("Size of Tickets found for Description: " + templist.size());
                ticketSet.addAll(templist);
                ticketSet.addAll(ticketsTitle.get(1, TimeUnit.SECONDS));
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                return resolveSearchSimpleInfo();
            }
        }

        if(ticketSet.size() == 0){
            return resolveSearchSimpleInfo();
        }
        foundTickets = new ArrayList<>(ticketSet);
        LOG.info("Tickets aus Suche gefunden: " + foundTickets.size());
        searchString = null;
        // TODO: return definieren (eigenes XHTML?)
        return FacesContextUtils.resolveInfo(UITexts.SIMPLE_SEARCH_SUMMARY,
                UITexts.SIMPLE_SEARCH_DETAIL, null);
    }

    private String resolveSearchSimpleInfo() {
        return FacesContextUtils.resolveInfo(UITexts.SIMPLE_SEARCH_NOT_FOUND_SUMMARY,
                UITexts.SIMPLE_SEARCH_NOT_FOUND_DETAIL, null);
    }

    public void searchDetails(){

    }

    public List<TicketDTO> getFoundTickets() {
        return foundTickets;
    }

    public void setFoundTickets(List<TicketDTO> foundTickets) {
        this.foundTickets = foundTickets;
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
