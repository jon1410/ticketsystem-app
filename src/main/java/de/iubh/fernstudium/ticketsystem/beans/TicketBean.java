package de.iubh.fernstudium.ticketsystem.beans;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
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
    private UserService userService;
    @Inject
    private DefaultInits defaultInits;
    @Inject
    private UserDataBean userDataBean;

    private String newComment;
    private String userIdCommentor;
    private String idAssigneeNewTicket;

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

    public String getIdAssigneeNewTicket() {
        return idAssigneeNewTicket;
    }

    public void setIdAssigneeNewTicket(String idAssigneeNewTicket) {
        this.idAssigneeNewTicket = idAssigneeNewTicket;
    }

    public void createTicket(){

        super.setReporter(currentUserBean);
        super.setCreationTime(LocalDateTime.now());
        super.setTicketStatus(TicketStatus.NEW);
        if(StringUtils.isEmpty(idAssigneeNewTicket)){
            super.setAssignee(defaultInits.getDefaultAssginee());
        }
        else{
            try {
                super.setReporter(userService.getUserByUserId(idAssigneeNewTicket));
            } catch (UserNotExistsException e) {
                LOG.error(ExceptionUtils.getRootCauseMessage(e));
            }
        }
        TicketDTO ticketDTO = ticketService.createTicket(this);
        userDataBean.addTicket(ticketDTO);
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

    public String createMasterticket(){

        return null;
    }

}
