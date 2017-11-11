package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UITexts;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;
import de.iubh.fernstudium.ticketsystem.dtos.HistoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.services.HistoryService;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import de.iubh.fernstudium.ticketsystem.services.impl.EventProducer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("userDataBean")
@SessionScoped
public class UserDataBean implements Serializable {

    private static final Logger LOG = LogManager.getLogger(UserDataBean.class);

    @Inject
    private TicketService ticketService;
    @Inject
    private CurrentUserBean currentUserBean;
    @EJB
    private EventProducer eventProducer;
    @Inject
    private HistoryService historyService;

    private List<TicketDTO> tickets; // entspricht den offenen Tickets des aktuellen Users
    private List<TicketDTO> reportedByLoggedInUser;
    private TicketDTO activeTicket;
    private List<HistoryDTO> historyOfActiveTicket;
    private String newComment;
    private List<Long> childTickets;
    
    private TicketDTO selektiertesTicket;
    
    
    public void showDetail(TicketDTO ticket){
    this.selektiertesTicket = ticket;
        
        //Öffnet das Modal
        RequestContext requestContext = RequestContext.getCurrentInstance();    
        requestContext.execute("$('.detailModal').modal('show');");
    }

    public void init(String userId){

        LOG.info("Initializing Tickets for userId: " + userId);
        try {
            tickets = ticketService.getOpenTicketsForUserId(userId);
            reportedByLoggedInUser = ticketService.getTicketsReportedByUserId(userId); //ToDO: evtl. hier Liste truncaten???
            LOG.info(String.format("%d Tickets loaded for User: %s", tickets.size(), userId));
        } catch (UserNotExistsException e) {
            LOG.error(ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public void terminateTicket(TicketDTO ticketDTO){
        try {
            ticketService.changeStatus(ticketDTO.getId(), TicketStatus.UST);
        } catch (NoSuchTicketException e) {
            FacesContextUtils.resolveError(UITexts.STOP_TICKET_ERROR_SUMMARY, UITexts.STOP_TICKET_ERROR_DETAIL, null);
        }
        tickets.remove(ticketDTO);
        eventProducer.produceHistoryEvent(ticketDTO.getId(), HistoryAction.UC, null);
    }

    public void startProgress(){
        changeStatus(TicketStatus.IPU);
    }


    public void resolveTicket(){
        changeStatus(TicketStatus.RES);
    }

    public void finishTicket(){
        changeStatus(TicketStatus.CLO);
    }

    public void addTicket(TicketDTO ticketDTO){
        if(tickets == null){
            tickets = new ArrayList<>();
        }
        tickets.add(ticketDTO);
    }

    public void createMasterTicket(){
        if(hasActiveTicket()){
            if (CollectionUtils.isNotEmpty(childTickets)) {
                try {
                    TicketDTO ticketDTO = ticketService.createMasterTicket(activeTicket.getId(), childTickets);
                    childTickets = null;
                    updateCache(ticketDTO);
                } catch (NoSuchTicketException e) {
                    LOG.error(ExceptionUtils.getRootCauseMessage(e));
                    FacesContextUtils.resolveError(UITexts.ERR_MASTER_TICKET,
                            UITexts.ERR_MASTER_TICKET, null);
                }
            } else{
                FacesContextUtils.resolveError(UITexts.ERR_MASTER_TICKET_NO_CHILD,
                        UITexts.ERR_MASTER_TICKET_NO_CHILD, null);
            }
        }
    }

    public void addChildTicket(TicketDTO ticketDTO){
        if(CollectionUtils.isEmpty(childTickets)){
            childTickets = new ArrayList<>();
        }
        childTickets.add(ticketDTO.getId());
    }

    public void addComment(){

        if(hasActiveTicket()) {
            try {
                TicketDTO ticketDTO = ticketService.addComment(activeTicket.getId(), this.newComment, currentUserBean.getUserId());
                updateCache(ticketDTO);
                fireEvent(activeTicket.getId(), HistoryAction.CA, "Kommentar: " + newComment);
                newComment = null;
            } catch (NoSuchTicketException | UserNotExistsException e) {
                LOG.error(ExceptionUtils.getRootCauseMessage(e));
                FacesContextUtils.resolveError(UITexts.ERROR_NEW_COMMENT,
                        UITexts.ERROR_NEW_COMMENT, null);
            }
        }
    }

    public void updateCache(TicketDTO dto){
        for(int i=0; i<tickets.size(); i++){
            TicketDTO t = tickets.get(i);
            if(t.getId() == dto.getId()){
                tickets.set(i, dto);
                break;
            }
        }
    }

    public List<TicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }

    public TicketDTO getActiveTicket() {
        return activeTicket;
    }

    public void setActiveTicket(TicketDTO activeTicket) {
        this.activeTicket = activeTicket;
        initHistoryOfActiveTicket(activeTicket.getId());
    }

    public String getNewComment() {
        return newComment;
    }

    public void setNewComment(String newComment) {
        this.newComment = newComment;
    }

    public List<HistoryDTO> getHistoryOfActiveTicket() {
        if(CollectionUtils.isEmpty(historyOfActiveTicket)){
            if(activeTicket != null){
                historyOfActiveTicket = historyService.getHistoryForTicket(activeTicket.getId());
            }else{
                historyOfActiveTicket = new ArrayList<>();
            }
        }
        return historyOfActiveTicket;
    }

    public void setHistoryOfActiveTicket(List<HistoryDTO> historyOfActiveTicket) {
        this.historyOfActiveTicket = historyOfActiveTicket;
    }

    public List<TicketDTO> getReportedByLoggedInUser() {
        return reportedByLoggedInUser;
    }

    public void setReportedByLoggedInUser(List<TicketDTO> reportedByLoggedInUser) {
        this.reportedByLoggedInUser = reportedByLoggedInUser;
    }

    public void initHistoryOfActiveTicket(Long id) {
        this.historyOfActiveTicket = historyService.getHistoryForTicket(id);
    }

    public List<Long> getChildTickets() {
        return childTickets;
    }

    public void setChildTickets(List<Long> childTickets) {
        this.childTickets = childTickets;
    }

    private void fireEvent(Long ticketId, HistoryAction historyAction, String details){
        eventProducer.produceHistoryEvent(ticketId, historyAction, details);
    }
    
    
    public TicketDTO getSelektiertesTicket() {
        return selektiertesTicket;
    }

    public void setSelektiertesTicket(TicketDTO ticket) {
        this.selektiertesTicket = ticket;
    }

    private void changeStatus(TicketStatus newStatus) {

        if(hasActiveTicket()){
            try {
                ticketService.changeStatus(activeTicket.getId(), newStatus);
                fireEvent(activeTicket.getId(), HistoryAction.SC, "Neuer Status: " + newStatus.getResolvedText());
            } catch (NoSuchTicketException e) {
                LOG.error(ExceptionUtils.getRootCauseMessage(e));
                FacesContextUtils.resolveError(UITexts.CHANGE_STAT_ERROR, UITexts.CHANGE_STAT_ERROR, null);
            }
        }
    }

    private boolean hasActiveTicket(){
        if(activeTicket == null){
            FacesContextUtils.resolveError(UITexts.NO_ACTIVE_TICKET,
                    UITexts.NO_ACTIVE_TICKET, null);
            return false;
        }
        return true;
    }
}
