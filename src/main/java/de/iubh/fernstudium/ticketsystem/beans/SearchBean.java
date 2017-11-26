package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.db.utils.CustomNativeQuery;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UITexts;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import de.iubh.fernstudium.ticketsystem.services.SearchService;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

/**
 * Bean, welches die Funktionalität für die Suche bereitstellt
 */
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
    @Inject
    private CategoryService categoryService;

    //Detail-Suche
    private String dateFrom;
    private String dateTo;
    private List<String> stati;
    private String selectedStatiForSearch;
    private String[] selectedStati;
    private String userIdReporter;
    private String userIdAssignee;
    private String categoryText;

    private String searchString;
    private List<TicketDTO> foundTickets;

    @PostConstruct
    public void initStatusValues(){
        TicketStatus[] ticketStati = TicketStatus.values();
        stati = new ArrayList<>(ticketStati.length);

        for(int i=0; i<ticketStati.length; i++){
            stati.add(ticketStati[i].getResolvedText());
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
                return resolveSearchInfo(UITexts.SIMPLE_SEARCH_NOT_FOUND_DETAIL);
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
                return resolveSearchInfo(UITexts.SIMPLE_SEARCH_NOT_FOUND_DETAIL);
            }
        }

        if(ticketSet.isEmpty()){
            return resolveSearchInfo(UITexts.SIMPLE_SEARCH_NOT_FOUND_DETAIL);
        }
        foundTickets = new ArrayList<>(ticketSet);
        LOG.info("Tickets aus Suche gefunden: " + foundTickets.size());
        searchString = null;

        return FacesContextUtils.resolveInfo(UITexts.SEARCH_SUMMARY,
                UITexts.SEARCH_DETAIL, null);
    }


    private String resolveSearchInfo(String detailText) {
        return FacesContextUtils.resolveInfo(UITexts.SEARCH_NOT_FOUND_SUMMARY,
                detailText, null);
    }

    public String searchDetails(){

        //um nicht den gesamten Code zu ändern, kleiner Hack
        if(StringUtils.isNotEmpty(selectedStatiForSearch)){
            selectedStati = StringUtils.split(selectedStatiForSearch, ",");
        }

        Future<List<TicketDTO>> tickets;
        List<TicketDTO> foundTicketsFromSearch;
        LocalDateTime ldtFrom;
        LocalDateTime ldtTo;
        if(StringUtils.isNotEmpty(dateFrom)){
            ldtFrom = DateTimeUtil.format(dateFrom);
        }else{
            ldtFrom = LocalDate.now().with(firstDayOfYear()).atStartOfDay();
        }

        if(StringUtils.isNotEmpty(dateTo)){
            ldtTo = DateTimeUtil.format(dateTo);
        }else{
            ldtTo = LocalDateTime.now();
        }

        CustomNativeQuery.QueryBuilder queryBuilder = CustomNativeQuery.builder()
                .selectAll().from("TICKET").where("CREA_TSP")
                .between(DateTimeUtil.localDtToSqlTimestamp(ldtFrom), DateTimeUtil.localDtToSqlTimestamp(ldtTo));


        if(StringUtils.isNotEmpty(userIdReporter)){
            try {
                UserDTO user = userService.getUserByUserId(userIdReporter);
                queryBuilder = queryBuilder.and("reporter_USERID").isEqualTo(user.getUserId());
            } catch (UserNotExistsException e) {
                queryBuilder = queryBuilder.and("reporter_USERID").like(userIdReporter);
            }
        }

        if(StringUtils.isNotEmpty(userIdAssignee)){
            try {
                UserDTO user = userService.getUserByUserId(userIdAssignee);
                queryBuilder = queryBuilder.and("assignee_USERID").isEqualTo(user.getUserId());
            } catch (UserNotExistsException e) {
                queryBuilder = queryBuilder.and("assignee_USERID").like(userIdAssignee);
            }
        }

        if(StringUtils.isNotEmpty(categoryText)){
            List<CategoryDTO> categoryDTOList = categoryService.getCategoryByName(categoryText);
            if(CollectionUtils.isNotEmpty(categoryDTOList)){
                String[] catIds = categoryDTOList.stream().map(c -> c.getCategoryId()).toArray(String[]::new);
                queryBuilder = queryBuilder.and("category_CATEGID").in(catIds);
            }
        }

        List<String> statusValuesAsString = null;
        if(selectedStati != null && selectedStati.length  > 0){
            statusValuesAsString = new ArrayList<>(selectedStati.length);
            for(String statusValue : selectedStati){
                statusValuesAsString.add(TicketStatus.fromString(statusValue).toString());
            }
        }

        if(statusValuesAsString != null && !statusValuesAsString.isEmpty()){
            queryBuilder = queryBuilder.and("STATUS").in(statusValuesAsString.toArray(new String[0])); //siehe https://shipilev.net/blog/2016/arrays-wisdom-ancients/
        }
        CustomNativeQuery customNativeQuery = queryBuilder.buildQuery();
        LOG.info("Starting search Service Details: " + customNativeQuery.getQueryString());
        LOG.info("Parameters are: " + customNativeQuery.getParameters().toString());
        tickets = searchService.searchByQuery(customNativeQuery.getQueryString(), customNativeQuery.getParameters());
        restoreSearchValuesToNull();

        try {
            foundTicketsFromSearch = tickets.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e ) {
            LOG.error("Exception during Async-Search " + ExceptionUtils.getRootCauseMessage(e));
            return resolveSearchInfo(UITexts.DETAIL_SEARCH_NOT_FOUND_DETAIL);
        }

        if(foundTicketsFromSearch.size() == 0){
            LOG.info("Size of found tickets is ZERO: ");
            return resolveSearchInfo(UITexts.DETAIL_SEARCH_NOT_FOUND_DETAIL);
        }
        LOG.info("Size of Tickets in Detailsearch: " + foundTicketsFromSearch.size());
        this.foundTickets = foundTicketsFromSearch;

        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("$('.ergebnisModal').modal('show');");

        return FacesContextUtils.resolveInfo(UITexts.SEARCH_SUMMARY,
                UITexts.SEARCH_DETAIL, null);
    }

    private void restoreSearchValuesToNull() {
        this.dateTo = null;
        this.dateFrom = null;
        this.userIdAssignee = null;
        this.userIdReporter = null;
        this.selectedStati = null;
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

    public List<String> getStati() {
        return stati;
    }

    public void setStati(List<String> stati) {
        this.stati = stati;
    }

    public String getSelectedStatiForSearch() {
        return selectedStatiForSearch;
    }

    public void setSelectedStatiForSearch(String selectedStatiForSearch) {
        this.selectedStatiForSearch = selectedStatiForSearch;
    }

    public String[] getSelectedStati() {
        return selectedStati;
    }

    public void setSelectedStati(String[] selectedStati) {
        this.selectedStati = selectedStati;
    }

    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }
}
