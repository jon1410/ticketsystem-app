package de.iubh.fernstudium.ticketsystem.dtos;

import de.iubh.fernstudium.ticketsystem.db.entities.CommentEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.services.TicketDBService;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;

import javax.inject.Inject;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanj on 03.07.2017.
 */
public class TicketDTO implements Serializable {

    @Inject
    private TicketDBService ticketDBService;

    private Long id;
    private String title;
    private String description;
    private TicketStatus ticketStatus;
    private UserDTO reporter;
    private LocalDateTime creationTime;
    private CategoryDTO category;
    private UserDTO assignee;
    private List<CommentDTO> comments;
    private List<Long> childTicketsIds;
    private Long masterTicketId;
    private String creationTimeAsString;

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
                     List<CommentDTO> comments, List<Long> childTickets, Long masterTicket) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ticketStatus = ticketStatus;
        this.reporter = reporter;
        this.creationTime = creationTime;
        this.category = category;
        this.assignee = assignee;
        this.comments = comments;
        this.childTicketsIds = childTickets;
        this.masterTicketId = masterTicket;
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

    public UserDTO getReporter() {
        return reporter;
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

    public UserDTO getAssignee() {
        return assignee;
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

    public String getCreationTimeAsString() {
        return creationTime.toString();
    }

    public void setCreationTimeAsString(String creationTimeAsString) {
        this.creationTimeAsString = creationTimeAsString;
    }

    public List<Long> getChildTicketsIds() {
        return childTicketsIds;
    }

    public void setChildTicketsIds(List<Long> childTicketsIds) {
        this.childTicketsIds = childTicketsIds;
    }

    public Long getMasterTicketId() {
        return masterTicketId;
    }

    public void setMasterTicketId(Long masterTicketId) {
        this.masterTicketId = masterTicketId;
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
        if(childTicketsIds != null && childTicketsIds.size() > 0){
            children = new ArrayList<>(childTicketsIds.size());
            for(Long id : childTicketsIds){
                TicketEntity t = ticketDBService.getTicketById(id);
                children.add(t);
            }
            //wenn ein Ticket "Kinder" hat ist es automatisch ein Master-Ticket
            //und kann selbst keine Master-Referenz haben
            masterTicketId = null;
        }else{
            if(masterTicketId != null ){
                ticketEntity = ticketDBService.getTicketById(masterTicketId);
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
                ", childTicketsIds=" + childTicketsIds +
                ", masterTicketId=" + masterTicketId +
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
        if (childTicketsIds != null ? !childTicketsIds.equals(ticketDTO.childTicketsIds) : ticketDTO.childTicketsIds != null)
            return false;
        return masterTicketId != null ? masterTicketId.equals(ticketDTO.masterTicketId) : ticketDTO.masterTicketId == null;
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
        result = 31 * result + (childTicketsIds != null ? childTicketsIds.hashCode() : 0);
        result = 31 * result + (masterTicketId != null ? masterTicketId.hashCode() : 0);
        return result;
    }
}
