package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("ticketBean")
@RequestScoped
public class TicketBean extends TicketDTO implements Serializable {

    private static final Logger LOG = LogManager.getLogger(TicketBean.class);

    @Inject
    private TicketService ticketService;

    private String newComment;
    private String userIdCommentor;

    public String getNewComment() {
        return newComment;
    }

    public void setNewComment(String newComment) {
        this.newComment = newComment;
    }

    public String getUserIdCommentor() {
        return userIdCommentor;
    }

    public void setUserIdCommentor(String userIdCommentor) {
        this.userIdCommentor = userIdCommentor;
    }

    public void getTicketDetailsById(){

    }

    public void createTicket(){
        TicketDTO ticketDTO = ticketService.createTicket(this);
    }

    public String addComment(){
        try {
            ticketService.addComment(super.getId(), this.newComment, userIdCommentor);
            //TODO: return Wert definieren
            return "main.xhtml";
        } catch (NoSuchTicketException | UserNotExistsException e) {
            LOG.error(ExceptionUtils.getRootCauseMessage(e));
            return FacesContextUtils.resolveError("Kommentar konnte nicht hinzugefügt werden",
                    "Kommentar konnte nicht hinzugefügt werden", null);
        }

    }

}
