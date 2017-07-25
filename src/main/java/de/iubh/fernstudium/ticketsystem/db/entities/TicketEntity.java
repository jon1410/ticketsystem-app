package de.iubh.fernstudium.ticketsystem.db.entities;

import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.dtos.CommentDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanj on 16.07.2017.
 */
@Entity
@Table(name = "TICKET")
@NamedQueries({
         @NamedQuery(name = "getTicketsForUserIdAndStatus", query = "select t from TicketEntity t where t.reporter = :userid and t.ticketStatus in :statusList"),
})
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "TITLE", length = 64, nullable = false)
    private String title;

    @Column(name = "DESC", length = 1000, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STAUTS", nullable = false)
    private TicketStatus ticketStatus;

    @OneToOne(fetch = FetchType.EAGER)
    private UserEntity reporter;

    @Column(name = "CREA_TSP", nullable = false)
    private Timestamp creationTime;

    @Column(name = "CATEG", nullable = false)
    private String category; //evtl. ReferenzID auf Kategorie

    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity assignee;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CommentEntity> comments = new ArrayList<>();

    public TicketEntity() {
    }

    public TicketEntity(String title, String description, TicketStatus ticketStatus, UserEntity reporter, Timestamp creationTime, String category, UserEntity assignee, List<CommentEntity> comments) {
        this.title = title;
        this.description = description;
        this.ticketStatus = ticketStatus;
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

    public UserEntity getReporter() {
        return reporter;
    }

    public void setReporter(UserEntity reporter) {
        this.reporter = reporter;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public UserEntity getAssignee() {
        return assignee;
    }

    public void setAssignee(UserEntity assignee) {
        this.assignee = assignee;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    public TicketDTO toDto(){
        List<CommentDTO> commentDTOList = new ArrayList<>(comments.size());
        for(CommentEntity c : comments){
            commentDTOList.add(c.toDto());
        }
        return new TicketDTO(id, title, description,reporter.toDto(),
                DateTimeUtil.sqlTimestampToLocalDate(creationTime), category, assignee.toDto(), commentDTOList);
    }
}
