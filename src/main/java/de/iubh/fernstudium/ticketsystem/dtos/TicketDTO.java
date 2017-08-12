package de.iubh.fernstudium.ticketsystem.dtos;

import de.iubh.fernstudium.ticketsystem.db.entities.CommentEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
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
    private String category; //evtl. ReferenzID auf Kategorie
    private UserDTO assignee;
    private List<CommentDTO> comments;

    public TicketDTO() {
    }

    public TicketDTO(Long id, String title, String description, UserDTO reporter, LocalDateTime creationTime, String category, UserDTO assignee, List<CommentDTO> comments) {
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
        //TODO: getUserFromDB
        this.reporter = reporter;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAssignee() {
        return assignee.getUserId();
    }

    public void setAssignee(UserDTO assignee) {
        //TODO: getFromDB
        this.assignee = assignee;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    /**
     * Erzeugt eine Entity aus dem DTO
     */
    public TicketEntity toEntity(){
        List<CommentEntity> commentEntities = null;
        if(comments != null & comments.size() > 0){
            commentEntities = new ArrayList<>(comments.size());
            for(CommentDTO c : comments){
                commentEntities.add(c.toEntity());
            }
        }
        return new TicketEntity(title, description, ticketStatus,
                reporter.toEntity(), DateTimeUtil.localDtToSqlTimestamp(creationTime),
                category, assignee.toEntity(), commentEntities);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketDTO ticketDTO = (TicketDTO) o;

        if (id != null ? !id.equals(ticketDTO.id) : ticketDTO.id != null) return false;
        if (title != null ? !title.equals(ticketDTO.title) : ticketDTO.title != null) return false;
        if (reporter != null ? !reporter.equals(ticketDTO.reporter) : ticketDTO.reporter != null) return false;
        if (creationTime != null ? !creationTime.equals(ticketDTO.creationTime) : ticketDTO.creationTime != null)
            return false;
        if (category != null ? !category.equals(ticketDTO.category) : ticketDTO.category != null) return false;
        if (assignee != null ? !assignee.equals(ticketDTO.assignee) : ticketDTO.assignee != null) return false;
        return comments != null ? comments.equals(ticketDTO.comments) : ticketDTO.comments == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (reporter != null ? reporter.hashCode() : 0);
        result = 31 * result + (creationTime != null ? creationTime.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (assignee != null ? assignee.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", assignee='" + assignee + '\'' +
                ", comments=" + comments +
                '}';
    }
}
