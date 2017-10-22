package de.iubh.fernstudium.ticketsystem.db.service.test;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.CommentEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.TicketDBService;
import de.iubh.fernstudium.ticketsystem.db.services.UserDBService;
import de.iubh.fernstudium.ticketsystem.db.services.impl.TicketDBServiceImpl;
import de.iubh.fernstudium.ticketsystem.db.services.impl.UserDBServiceImpl;
import de.iubh.fernstudium.ticketsystem.db.util.JPAHibernateTestManager;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;
import org.h2.tools.RunScript;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.powermock.reflect.Whitebox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TicketDBServiceTest extends JPAHibernateTestManager {

    private TicketDBService ticketDBService = new TicketDBServiceImpl();
    private UserDBService userDBService = new UserDBServiceImpl();
    private static boolean isDataLoaded = false;

    @Before
    public void initEntityManager(){
        Whitebox.setInternalState(ticketDBService, "em", super.em);
        Whitebox.setInternalState(userDBService, "em", super.em);

        if(!isDataLoaded) {
            Session session = em.unwrap(Session.class);
            session.doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    File script = new File(getClass().getResource("/initScript.sql").getFile());
                    try {
                        RunScript.execute(connection, new FileReader(script));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            isDataLoaded = true;
        }
    }

    @Test
    public void test1CreateTicket(){
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setComments(null);
        ticketEntity.setTicketStatus(TicketStatus.NEW);
        ticketEntity.setAssignee(getUser("tutor"));
        ticketEntity.setCategory(new CategoryEntity("ISEF", "Test ISEF", getUser("tutor")));
        ticketEntity.setCreationTime(DateTimeUtil.localDtToSqlTimestamp(LocalDateTime.now()));
        ticketEntity.setDescription("Ein Testticket");
        ticketEntity.setReporter(getUser("student"));
        ticketEntity.setTitle("Testticket");
        em.getTransaction().begin();
        TicketEntity t = ticketDBService.createTicket(ticketEntity);
        System.out.println("TicketID: " + t.getId());
        em.getTransaction().commit();
    }

    @Test
    public void test2GetTicketById(){

        TicketEntity t = ticketDBService.getTicketById(1L);
        Assert.assertEquals("tutor", t.getAssignee().getUserId());
        Assert.assertEquals("Testticket", t.getTitle());
        Assert.assertTrue(t.getComments() == null);
    }

    @Test
    public void test3GetOpenTicketsForUserId(){
        List<TicketEntity> tickets = ticketDBService.getOpenTicketsForUserId(getUser("tutor"));
        Assert.assertTrue(tickets.size() == 1);
    }

    @Test
    public void test40ChangeStatus(){
        em.getTransaction().begin();
        Assert.assertTrue( ticketDBService.changeStauts(1L, TicketStatus.CLO));
        em.getTransaction().commit();
    }

    @Test
    public void test41GetOpenTicketsForUserIdNotAvailable(){
        List<TicketEntity> tickets = ticketDBService.getOpenTicketsForUserId(getUser("tutor"));
        Assert.assertTrue(tickets.size() == 0);
    }

    @Test
    public void test50AddComment(){
        CommentEntity commentEntity = new CommentEntity(DateTimeUtil.now(), getUser("admin"), "I am a Comment", DateTimeUtil.now());
        em.getTransaction().begin();
        Assert.assertTrue(ticketDBService.addComment(1L, commentEntity));
        em.getTransaction().commit();
    }

    @Test
    public void test51TicketHasComments(){
        TicketEntity t = ticketDBService.getTicketById(1L);
        Assert.assertTrue(t.getComments().size() == 1);
        Assert.assertEquals("I am a Comment", t.getComments().get(0).getComment());
    }

    private UserEntity getUser(String id) {
        return userDBService.findById(id);
    }
}
