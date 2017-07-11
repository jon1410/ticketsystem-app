package de.iubh.fernstudium.ticketsystem.dtos;

import java.util.Calendar;

/**
 * Created by ivanj on 08.07.2017.
 */
public class CommentDTO {

    private Long id;
    private Calendar creationDate;
    private UserDTO author;
    private String comment;
    private Calendar changeDate;

    public CommentDTO() {
    }

    public CommentDTO(Calendar creationDate, UserDTO author, String comment, Calendar changeDate) {
        this.creationDate = creationDate;
        this.author = author;
        this.comment = comment;
        this.changeDate = changeDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Calendar getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Calendar changeDate) {
        this.changeDate = changeDate;
    }

    public void toEntity(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommentDTO that = (CommentDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        return changeDate != null ? changeDate.equals(that.changeDate) : that.changeDate == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (changeDate != null ? changeDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", author=" + author +
                ", comment='" + comment + '\'' +
                ", changeDate=" + changeDate +
                '}';
    }
}
