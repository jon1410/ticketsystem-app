package de.iubh.fernstudium.ticketsystem.dtos;

import de.iubh.fernstudium.ticketsystem.db.entities.CommentEntity;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Data-Transfer-Object für einen Kommentar
 */
public class CommentDTO {

    private Long id;
    private LocalDateTime creationDate;
    private UserDTO author;
    private String comment;
    private LocalDateTime changeDate;

    public CommentDTO() {
    }

    public CommentDTO(UserDTO author, String comment) {
        init(LocalDateTime.now(), author, comment, LocalDateTime.now());
    }

    public CommentDTO(LocalDateTime creationDate, UserDTO author, String comment, LocalDateTime changeDate) {
       init(creationDate, author, comment, changeDate);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getCreationDateAsString(){
        return DateTimeUtil.formatForUI(creationDate);
    }

    public void setCreationDate(LocalDateTime creationDate) {
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

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public String getChangeDateAsString(){
        return DateTimeUtil.formatForUI(changeDate);
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }

    public CommentEntity toEntity(){
        return new CommentEntity(DateTimeUtil.localDtToSqlTimestamp(creationDate), author.toEntity(), comment, DateTimeUtil.localDtToSqlTimestamp(changeDate));
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

    private void init(LocalDateTime creationDate, UserDTO author, String comment, LocalDateTime changeDate) {
        this.creationDate = creationDate;
        this.author = author;
        this.comment = comment;
        this.changeDate = changeDate;
    }
}
