package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UITexts;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.services.SearchService;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @Inject
    private UserService userService;

    //Detail-Suche
    private String dateFrom; //muss dd.MM.yyyy sein
    private String dateTo; //muss dd.MM.yyyy sein
    private Map<String, Object> statusValues;
    private String userIdReporter;
    private String userIdAssignee;

    private String searchString;
    private List<TicketDTO> foundTickets;

    @PostConstruct
    public void initStatusValues(){
        TicketStatus[] ticketStati = TicketStatus.values();
        statusValues = new LinkedHashMap<>(ticketStati.length);

        for(TicketStatus t : ticketStati){
            statusValues.put(t.getResolvedText(), t);
        }
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
        // TODO: return definieren (eigenes XHTML oder Modal?)
        return FacesContextUtils.resolveInfo(UITexts.SIMPLE_SEARCH_SUMMARY,
                UITexts.SIMPLE_SEARCH_DETAIL, null);
    }


    private String resolveSearchSimpleInfo() {
        return FacesContextUtils.resolveInfo(UITexts.SIMPLE_SEARCH_NOT_FOUND_SUMMARY,
                UITexts.SIMPLE_SEARCH_NOT_FOUND_DETAIL, null);
    }

    public String searchDetails(){

        Set<TicketDTO> ticketSet = new HashSet<>();
        List<TicketDTO> ticketsByDateRange;

        Future<List<TicketDTO>> ticketsDateRange;
        Future<List<TicketDTO>> ticketsReporter;
        Future<List<TicketDTO>> ticketsAssignee;


        if(isDateTimeSet()){
            LocalDateTime ldtFrom = DateTimeUtil.format(dateFrom);

            LocalDateTime ldtTo;
            if(dateTo == null && StringUtils.isEmpty(dateTo)){
                ldtTo = LocalDateTime.now();
            }else{
                ldtTo = DateTimeUtil.format(dateTo);
            }
            ticketsDateRange = searchService.searchByDateRange(ldtFrom, ldtTo);
        }

        if(StringUtils.isNotEmpty(userIdReporter)){
            ticketsReporter = searchService.searchByReporter(userIdReporter);
        }

        if(StringUtils.isNotEmpty(userIdAssignee)){
            ticketsAssignee = searchService.searchByAssignee(userIdAssignee);
        }

        return null;
    }

    private boolean isDateTimeSet() {
        return dateFrom != null && StringUtils.isNotEmpty(dateFrom);
    }

    public List<TicketDTO> getFoundTickets() {
        return foundTickets;
    }

    public void setFoundTickets(List<TicketDTO> foundTickets) {
        this.foundTickets = foundTickets;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public Map<String, Object> getStatusValues() {
        return statusValues;
    }

    public void setStatusValues(Map<String, Object> statusValues) {
        this.statusValues = statusValues;
    }

    public String getUserIdReporter() {
        return userIdReporter;
    }

    public void setUserIdReporter(String userIdReporter) {
        this.userIdReporter = userIdReporter;
    }

    public String getUserIdAssignee() {
        return userIdAssignee;
    }

    public void setUserIdAssignee(String userIdAssignee) {
        this.userIdAssignee = userIdAssignee;
    }
}
