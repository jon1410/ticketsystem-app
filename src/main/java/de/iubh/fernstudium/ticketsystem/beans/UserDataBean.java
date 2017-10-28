package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UITexts;
import de.iubh.fernstudium.ticketsystem.domain.event.payload.HistoryPayload;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("userDataBean")
@SessionScoped
public class UserDataBean implements Serializable {

    private static final Logger LOG = LogManager.getLogger(UserDataBean.class);

    @Inject
    private TicketService ticketService;
    @EJB
    private EventProducer eventProducer;
    @Inject
    private CurrentUserBean currentUserBean;

    List<TicketDTO> tickets;

    public void init(String userId){

        LOG.info("Initializing Tickets for userId: " + userId);
        try {
            tickets = ticketService.getOpenTicketsForUserId(userId);
            LOG.info(String.format("%d Tickets loaded for User: %s", tickets.size(), userId));
        } catch (UserNotExistsException e) {
            LOG.error(ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public List<TicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }

    public void terminateTicket(TicketDTO ticketDTO){
        try {
            ticketService.changeStatus(ticketDTO.getId(), TicketStatus.UST);
        } catch (NoSuchTicketException e) {
            FacesContextUtils.resolveError(UITexts.STOP_TICKET_ERROR_SUMMARY, UITexts.STOP_TICKET_ERROR_DETAIL, null);
        }
        tickets.remove(ticketDTO);
        eventProducer.produceHistoryEvent(new HistoryPayload(ticketDTO.getId(), HistoryAction.UC, currentUserBean.getUserId()));
    }

    public void addTicket(TicketDTO ticketDTO){
        if(tickets == null){
            tickets = new ArrayList<>();
        }
        tickets.add(ticketDTO);
    }
}
