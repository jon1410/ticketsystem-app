package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UITexts;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.HistoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import de.iubh.fernstudium.ticketsystem.services.HistoryService;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import de.iubh.fernstudium.ticketsystem.services.impl.EventProducer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Verwaltet die Daten des aktuell angemeldeten Benutzers
 */
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
    @Inject
    private CategoryService categoryService;

    private List<TicketDTO> tickets; // entspricht den offenen Tickets des aktuellen Users
    private List<TicketDTO> reportedByLoggedInUser;
    private TicketDTO activeTicket;
    private List<HistoryDTO> historyOfActiveTicket;
    private String newComment;
    private String newCategoryId;
    private long newMasterTicketId;
    private List<Long> childTickets;

    public String checkLoggedInUser() {
        if (currentUserBean == null || currentUserBean.getUserId() == null) {
            return FacesContextUtils.REDIRECT_LOGIN;
        }
        return null;
    }

    public void changeCateogry() {
        if (hasActiveTicket()) {
            if (StringUtils.isNotEmpty(newCategoryId)) {
                try {
                    CategoryDTO categoryDTO = categoryService.getCategoryById(newCategoryId);
                    TicketDTO ticketDTO = ticketService.changeCategoryOfTicket(activeTicket.getId(), categoryDTO);
                    activeTicket = ticketDTO;
                    updateCache(ticketDTO);
                    fireEvent(activeTicket.getId(), HistoryAction.CC, "Neue Kategorie: " + categoryDTO.getCategoryName());
                    FacesContextUtils.resolveInfo(UITexts.CHANGE_CATEGORY_OK, UITexts.CHANGE_CATEGORY_OK, null);
                } catch (CategoryNotFoundException | NoSuchTicketException e) {
                    LOG.error(ExceptionUtils.getRootCauseMessage(e));
                    FacesContextUtils.resolveError(UITexts.CHANGE_CATEGORY_ERROR, UITexts.CHANGE_CATEGORY_ERROR, null);
                }
            }else {
                FacesContextUtils.resolveError(UITexts.NO_NEW_CATEGORY, UITexts.NO_NEW_CATEGORY, null);
            }
        }
    }

    public void init(String userId) {

        LOG.info("Initializing Tickets for userId: " + userId);
        try {
            tickets = ticketService.getOpenTicketsForUserId(userId);
            reportedByLoggedInUser = ticketService.getTicketsReportedByUserId(userId);
            LOG.info(String.format("%d Tickets loaded for User: %s", tickets.size(), userId));
        } catch (UserNotExistsException e) {
            LOG.error(ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public void terminateActiveTicket() {
        if (hasActiveTicket()) {
            terminateTicket(activeTicket);
        }
    }

    public void terminateTicket(TicketDTO ticketDTO) {
        try {
            TicketStatus ticketStatus;
            if(currentUserBean.getUserRole() == UserRole.ST){
                ticketStatus = TicketStatus.UST;
            }else{
                ticketStatus = TicketStatus.UTU;
            }
            ticketService.changeStatus(ticketDTO.getId(), ticketStatus);
            activeTicket.setTicketStatus(ticketStatus);
        } catch (NoSuchTicketException e) {
            FacesContextUtils.resolveError(UITexts.STOP_TICKET_ERROR_SUMMARY, UITexts.STOP_TICKET_ERROR_DETAIL, null);
        }
        removeTicketFromCache(ticketDTO.getId());
        eventProducer.produceHistoryEvent(ticketDTO.getId(), HistoryAction.UC, null);
    }

    public void startProgress() {

        changeStatus(TicketStatus.IPU);
    }

    public void resolveTicket() {
        UserDTO newAssignee = activeTicket.getReporter();
        activeTicket.setAssignee(newAssignee);
        fireEvent(activeTicket.getId(), HistoryAction.AC, "Neuer Bearbeiter: " + newAssignee.getUserId());
        changeStatus(TicketStatus.RET);
    }

    public void finishTicket() {
        changeStatus(TicketStatus.CLO);
    }

    public void addTicket(TicketDTO ticketDTO) {
        if (tickets == null) {
            tickets = new ArrayList<>();
        }
        tickets.add(ticketDTO);
    }

    public void addTicketToReporter(TicketDTO ticketDTO) {
        if (reportedByLoggedInUser == null) {
            reportedByLoggedInUser = new ArrayList<>();
        }
        reportedByLoggedInUser.add(ticketDTO);
    }

    public void addMasterTicketToActiveTicket(){
        if (hasActiveTicket()) {
            if(newMasterTicketId > 0){
                try {
                    ticketService.createMasterTicket(newMasterTicketId, activeTicket.getId());
                    TicketDTO ticketDTO = ticketService.getTicketByID(activeTicket.getId());
                    activeTicket = ticketDTO;
                    updateCache(ticketDTO);
                    fireEvent(activeTicket.getId(), HistoryAction.CM, "MasterticketID: " + newMasterTicketId);
                } catch (NoSuchTicketException e) {
                    LOG.error(ExceptionUtils.getRootCauseMessage(e));
                    FacesContextUtils.resolveError(UITexts.ERR_MASTER_TICKET,
                            UITexts.ERR_MASTER_TICKET, null);
                }
            }
        }else{
            FacesContextUtils.resolveError(UITexts.NO_ACTIVE_TICKET,
                    UITexts.NO_ACTIVE_TICKET, null);
        }
    }

    public void createMasterTicket() {
        if (hasActiveTicket()) {
            if (CollectionUtils.isNotEmpty(childTickets)) {
                try {
                    TicketDTO ticketDTO = ticketService.createMasterTicket(activeTicket.getId(), childTickets);
                    childTickets = null;
                    updateCache(ticketDTO);
                } catch (NoSuchTicketException e) {
                    childTickets = null;
                    LOG.error(ExceptionUtils.getRootCauseMessage(e));
                    FacesContextUtils.resolveError(UITexts.ERR_MASTER_TICKET,
                            UITexts.ERR_MASTER_TICKET, null);
                }
            } else {
                childTickets = null;
                FacesContextUtils.resolveError(UITexts.ERR_MASTER_TICKET_NO_CHILD,
                        UITexts.ERR_MASTER_TICKET_NO_CHILD, null);
            }
        }
    }

    public void addChildTicket(TicketDTO ticketDTO) {
        if (CollectionUtils.isEmpty(childTickets)) {
            childTickets = new ArrayList<>();
        }
        childTickets.add(ticketDTO.getId());
    }

    public void addComment() {

        if (hasActiveTicket()) {
            try {
                TicketDTO ticketDTO = ticketService.addComment(activeTicket.getId(), this.newComment, currentUserBean.getUserId());
                activeTicket = ticketDTO;
                updateCache(ticketDTO);
                String historyDetails;
                if(newComment.length() < 980){
                    historyDetails = newComment;
                }else{
                    int i = ticketDTO.getComments().size() - 1;
                    historyDetails = "ID - " + ticketDTO.getComments().get(i).getId();
                }
                fireEvent(activeTicket.getId(), HistoryAction.CA, "Kommentar: " + historyDetails);
                newComment = null;
            } catch (NoSuchTicketException | UserNotExistsException e) {
                newComment = null;
                LOG.error(ExceptionUtils.getRootCauseMessage(e));
                FacesContextUtils.resolveError(UITexts.ERROR_NEW_COMMENT,
                        UITexts.ERROR_NEW_COMMENT, null);
            }
        }
    }

    public void showHistory() {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("$('.histModal').modal('show');");
    }

    public void updateCache(TicketDTO dto) {
        for (int i = 0; i < tickets.size(); i++) {
            TicketDTO t = tickets.get(i);
            if (t.getId() == dto.getId()) {
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
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("$('.detailModal').modal('show');");
    }

    public String getNewComment() {
        return newComment;
    }

    public void setNewComment(String newComment) {
        this.newComment = newComment;
    }

    public List<HistoryDTO> getHistoryOfActiveTicket() {
        if (CollectionUtils.isEmpty(historyOfActiveTicket)) {
            if (activeTicket != null) {
                historyOfActiveTicket = historyService.getHistoryForTicket(activeTicket.getId());
            } else {
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

    public String getNewCategoryId() {
        return newCategoryId;
    }

    public void setNewCategoryId(String newCategoryId) {
        this.newCategoryId = newCategoryId;
    }

    public long getNewMasterTicketId() {
        return newMasterTicketId;
    }

    public void setNewMasterTicketId(long newMasterTicketId) {
        this.newMasterTicketId = newMasterTicketId;
    }

    private void fireEvent(Long ticketId, HistoryAction historyAction, String details) {
        eventProducer.produceHistoryEvent(ticketId, historyAction, details);
    }

    private void changeStatus(TicketStatus newStatus) {

        if (hasActiveTicket()) {
            try {
                ticketService.changeStatus(activeTicket.getId(), newStatus);
                this.activeTicket.setTicketStatus(newStatus);
                fireEvent(activeTicket.getId(), HistoryAction.SC, "Neuer Status: " + newStatus.getResolvedText());
                if (newStatus == TicketStatus.CLO || newStatus == TicketStatus.UST) {
                    removeTicketFromCache(activeTicket.getId());
                } else {
                    updateCache(activeTicket);
                }
            } catch (NoSuchTicketException e) {
                LOG.error(ExceptionUtils.getRootCauseMessage(e));
                FacesContextUtils.resolveError(UITexts.CHANGE_STAT_ERROR, UITexts.CHANGE_STAT_ERROR, null);
            }
        }
    }

    private boolean hasActiveTicket() {
        if (activeTicket == null) {
            FacesContextUtils.resolveError(UITexts.NO_ACTIVE_TICKET,
                    UITexts.NO_ACTIVE_TICKET, null);
            return false;
        }
        return true;
    }

    private void removeTicketFromCache(Long ticketId) {
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getId() == ticketId) {
                tickets.remove(i);
            }
        }
    }
}
