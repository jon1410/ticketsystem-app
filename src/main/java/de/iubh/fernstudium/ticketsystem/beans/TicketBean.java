package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UITexts;
import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import de.iubh.fernstudium.ticketsystem.services.impl.EventProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;

@Named("ticketBean")
@RequestScoped
public class TicketBean extends TicketDTO implements Serializable {

    private static final Logger LOG = LogManager.getLogger(TicketBean.class);

    @Inject
    private TicketService ticketService;
    @Inject
    private CurrentUserBean currentUserBean;
    @Inject
    private UserDataBean userDataBean;
    @Inject
    private EventProducer eventProducer;

    public String createTicket(){

        super.setReporter(currentUserBean);
        super.setCreationTime(LocalDateTime.now());
        super.setTicketStatus(TicketStatus.NEW);
        super.setAssignee(getCategory().getTutor());

        TicketDTO ticketDTO = ticketService.createTicket(this);
        userDataBean.addTicket(ticketDTO);
        fireEvent(ticketDTO.getId(),  HistoryAction.CR);

        return FacesContextUtils.resolveInfo(UITexts.NEW_TICKET, UITexts.NEW_TICKET, null);
    }

    private void fireEvent(Long ticketId, HistoryAction historyAction){
        eventProducer.produceHistoryEvent(ticketId, historyAction);
    }

}
