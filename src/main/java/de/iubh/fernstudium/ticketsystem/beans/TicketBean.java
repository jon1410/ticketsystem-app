package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UITexts;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import de.iubh.fernstudium.ticketsystem.services.impl.EventProducer;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
    @Inject
    private CategoryService categoryService;

    private String categoryId;

    public String createTicket(){

        CategoryDTO categoryDTO = null;
        try {
            categoryDTO =  categoryService.getCategoryById(this.categoryId);
        } catch (CategoryNotFoundException e) {
            LOG.error(ExceptionUtils.getRootCause(e));
            return FacesContextUtils.resolveInfo(UITexts.NEW_TICKET_ERROR, UITexts.NEW_TICKET_ERROR, null);
        }
        super.setCategory(categoryDTO);
        super.setReporter(currentUserBean);
        super.setCreationTime(LocalDateTime.now());
        super.setTicketStatus(TicketStatus.NEW);
        super.setAssignee(categoryDTO.getTutor());

        TicketDTO ticketDTO = ticketService.createTicket(this);
        userDataBean.addTicketToReporter(ticketDTO);
        if(currentUserBean.getUserId().equals(ticketDTO.getAssignee().getUserId())){
            userDataBean.addTicket(ticketDTO);
        }
        fireEvent(ticketDTO.getId(),  HistoryAction.CR);

        return FacesContextUtils.resolveInfo(UITexts.NEW_TICKET, UITexts.NEW_TICKET, null);
    }

    private void fireEvent(Long ticketId, HistoryAction historyAction){
        eventProducer.produceHistoryEvent(ticketId, historyAction, null);
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TicketBean that = (TicketBean) o;

        return categoryId != null ? categoryId.equals(that.categoryId) : that.categoryId == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
        return result;
    }
}
