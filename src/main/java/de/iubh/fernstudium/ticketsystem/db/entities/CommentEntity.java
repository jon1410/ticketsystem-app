package de.iubh.fernstudium.ticketsystem.db.entities;

import de.iubh.fernstudium.ticketsystem.dtos.CommentDTO;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by ivanj on 16.07.2017.
 */
@Entity
@Table(name = "TICKET_COMMENT")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "COMMENTID")
    private Long id;

    @Column(name = "CREATION_TSP", nullable = false)
    private Timestamp creationDate;

    @OneToOne(fetch = FetchType.EAGER)
    private UserEntity author;

    @Column(name="COMMENT", length = 4000, nullable = false)
    private String comment;

    @Column(name = "CHG_TSP", nullable = false)
    private Timestamp changeDate;

    public CommentEntity() {
    }

    public CommentEntity(Timestamp creationDate, UserEntity author, String comment, Timestamp changeDate) {
        this.creationDate = creationDate;
        this.author = author;
        this.comment = comment;
        this.changeDate = changeDate;
    }

    public CommentEntity(LocalDateTime creationDate, UserEntity author, String comment, LocalDateTime changeDate) {
        this.creationDate = DateTimeUtil.localDtToSqlTimestamp(creationDate);
        this.author = author;
        this.comment = comment;
        this.changeDate = DateTimeUtil.localDtToSqlTimestamp(changeDate);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Timestamp changeDate) {
        this.changeDate = changeDate;
    }

    public CommentDTO toDto(){
        return new CommentDTO(DateTimeUtil.sqlTimestampToLocalDate(creationDate),
                author.toDto(), comment, DateTimeUtil.sqlTimestampToLocalDate(changeDate));
    }

}
