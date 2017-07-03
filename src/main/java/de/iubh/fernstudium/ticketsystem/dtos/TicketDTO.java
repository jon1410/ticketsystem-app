package de.iubh.fernstudium.ticketsystem.dtos;

import java.util.Calendar;
import java.util.List;

/**
 * Created by ivanj on 03.07.2017.
 */
public class TicketDTO {

    private Long id;
    private String title;
    private String reporter; //evtl. eigenes UserDTO
    private Calendar creationTime;
    private String category;
    private String assignee;
    private List<?> comments;

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

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public Calendar getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Calendar creationTime) {
        this.creationTime = creationTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public List<?> getComments() {
        return comments;
    }

    public void setComments(List<?> comments) {
        this.comments = comments;
    }

    /**
     * Erzeugt eine Entity aus dem DTO
     */
    public void toEntity(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketDTO ticketDTO = (TicketDTO) o;

        if (id != null ? !id.equals(ticketDTO.id) : ticketDTO.id != null) return false;
        if (category != null ? !category.equals(ticketDTO.category) : ticketDTO.category != null) return false;
        if (assignee != null ? !assignee.equals(ticketDTO.assignee) : ticketDTO.assignee != null) return false;
        return comments != null ? comments.equals(ticketDTO.comments) : ticketDTO.comments == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
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
