package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UITexts;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import de.iubh.fernstudium.ticketsystem.services.impl.EventProducer;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
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

    private List<TicketDTO> tickets;
    private TicketDTO activeTicket;
    private String newComment;

    public void init(String userId){

        LOG.info("Initializing Tickets for userId: " + userId);
        try {
            tickets = ticketService.getOpenTicketsForUserId(userId);
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
        eventProducer.produceHistoryEvent(ticketDTO.getId(), HistoryAction.UC);
    }

    public void addTicket(TicketDTO ticketDTO){
        if(tickets == null){
            tickets = new ArrayList<>();
        }
        tickets.add(ticketDTO);
    }

    public void addComment(){

        if(activeTicket == null){
            FacesContextUtils.resolveError(UITexts.NO_ACTIVE_TICKET,
                    UITexts.NO_ACTIVE_TICKET, null);
        }
        try {
            TicketDTO ticketDTO = ticketService.addComment(activeTicket.getId(), this.newComment, currentUserBean.getUserId());
            updateCache(ticketDTO);
            fireEvent(activeTicket.getId(), HistoryAction.CA);
        } catch (NoSuchTicketException | UserNotExistsException e) {
            LOG.error(ExceptionUtils.getRootCauseMessage(e));
            FacesContextUtils.resolveError(UITexts.ERROR_NEW_COMMENT,
                    UITexts.ERROR_NEW_COMMENT, null);
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
    }

    public String getNewComment() {
        return newComment;
    }

    public void setNewComment(String newComment) {
        this.newComment = newComment;
    }

    private void fireEvent(Long ticketId, HistoryAction historyAction){
        eventProducer.produceHistoryEvent(ticketId, historyAction);
    }
}
