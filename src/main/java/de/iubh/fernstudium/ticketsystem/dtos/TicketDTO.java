package de.iubh.fernstudium.ticketsystem.dtos;

import de.iubh.fernstudium.ticketsystem.db.entities.CommentEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanj on 03.07.2017.
 */
public class TicketDTO {

    private Long id;
    private String title;
    private String description;
    private TicketStatus ticketStatus;
    private UserDTO reporter;
    private LocalDateTime creationTime;
    private CategoryDTO category;
    private UserDTO assignee;
    private List<CommentDTO> comments;
    private List<TicketDTO> childTickets;
    private TicketDTO masterTicket;

    public TicketDTO() {
    }

    public TicketDTO(Long id, String title, String description, UserDTO reporter,
                     LocalDateTime creationTime, CategoryDTO category, UserDTO assignee, List<CommentDTO> comments) {
        this.id = id;
        this.title = title;
        this.description = description;
        ticketStatus = TicketStatus.NEW;
        this.reporter = reporter;
        this.creationTime = creationTime;
        this.category = category;
        this.assignee = assignee;
        this.comments = comments;
    }

    public TicketDTO(Long id, String title, String description, TicketStatus ticketStatus, UserDTO reporter,
                     LocalDateTime creationTime, CategoryDTO category, UserDTO assignee,
                     List<CommentDTO> comments, List<TicketDTO> childTickets, TicketDTO masterTicket) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ticketStatus = ticketStatus;
        this.reporter = reporter;
        this.creationTime = creationTime;
        this.category = category;
        this.assignee = assignee;
        this.comments = comments;
        this.childTickets = childTickets;
        this.masterTicket = masterTicket;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getReporter() {
        return reporter.getUserId();
    }

    public void setReporter(UserDTO reporter) {
        this.reporter = reporter;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public String getAssignee() {
        return assignee.getUserId();
    }

    public void setAssignee(UserDTO assignee) {
        this.assignee = assignee;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public List<TicketDTO> getChildTickets() {
        return childTickets;
    }

    public void setChildTickets(List<TicketDTO> childTickets) {
        this.childTickets = childTickets;
    }

    public TicketDTO getMasterTicket() {
        return masterTicket;
    }

    public void setMasterTicket(TicketDTO masterTicket) {
        this.masterTicket = masterTicket;
    }

    /**
     * Erzeugt eine Entity aus dem DTO
     */
    public TicketEntity toEntity(){
        List<CommentEntity> commentEntities = null;
        if(comments != null && comments.size() > 0){
            commentEntities = new ArrayList<>(comments.size());
            for(CommentDTO c : comments){
                commentEntities.add(c.toEntity());
            }
        }

        List<TicketEntity> children = null;
        TicketEntity ticketEntity = null;
        if(childTickets != null && childTickets.size() > 0){
            children = new ArrayList<>(childTickets.size());
            for(TicketDTO t : childTickets){
                children.add(t.toEntity());
            }
            //wenn ein Ticket "Kinder" hat ist es automatisch ein Master-Ticket
            //und kann selbst keine Master-Referenz haben
            masterTicket = null;
        }else{
            if(masterTicket != null){
                ticketEntity = masterTicket.toEntity();
            }
        }
        return new TicketEntity(title, description, ticketStatus,
                reporter.toEntity(), DateTimeUtil.localDtToSqlTimestamp(creationTime),
                category.toEntity(), assignee.toEntity(), commentEntities, children, ticketEntity);
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", ticketStatus=" + ticketStatus +
                ", reporter=" + reporter +
                ", creationTime=" + creationTime +
                ", category=" + category +
                ", assignee=" + assignee +
                ", comments=" + comments +
                ", childTickets=" + childTickets +
                ", masterTicket=" + masterTicket +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketDTO ticketDTO = (TicketDTO) o;

        if (id != null ? !id.equals(ticketDTO.id) : ticketDTO.id != null) return false;
        if (title != null ? !title.equals(ticketDTO.title) : ticketDTO.title != null) return false;
        if (description != null ? !description.equals(ticketDTO.description) : ticketDTO.description != null)
            return false;
        if (ticketStatus != ticketDTO.ticketStatus) return false;
        if (reporter != null ? !reporter.equals(ticketDTO.reporter) : ticketDTO.reporter != null) return false;
        if (creationTime != null ? !creationTime.equals(ticketDTO.creationTime) : ticketDTO.creationTime != null)
            return false;
        if (category != null ? !category.equals(ticketDTO.category) : ticketDTO.category != null) return false;
        if (assignee != null ? !assignee.equals(ticketDTO.assignee) : ticketDTO.assignee != null) return false;
        if (comments != null ? !comments.equals(ticketDTO.comments) : ticketDTO.comments != null) return false;
        if (childTickets != null ? !childTickets.equals(ticketDTO.childTickets) : ticketDTO.childTickets != null)
            return false;
        return masterTicket != null ? masterTicket.equals(ticketDTO.masterTicket) : ticketDTO.masterTicket == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (ticketStatus != null ? ticketStatus.hashCode() : 0);
        result = 31 * result + (reporter != null ? reporter.hashCode() : 0);
        result = 31 * result + (creationTime != null ? creationTime.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (assignee != null ? assignee.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (childTickets != null ? childTickets.hashCode() : 0);
        result = 31 * result + (masterTicket != null ? masterTicket.hashCode() : 0);
        return result;
    }
}
