package de.iubh.fernstudium.ticketsystem.db.service.test;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.CommentEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.CategoryDBService;
import de.iubh.fernstudium.ticketsystem.db.services.TicketDBService;
import de.iubh.fernstudium.ticketsystem.db.services.UserDBService;
import de.iubh.fernstudium.ticketsystem.db.services.impl.CategoryDBServiceImpl;
import de.iubh.fernstudium.ticketsystem.db.services.impl.TicketDBServiceImpl;
import de.iubh.fernstudium.ticketsystem.db.services.impl.UserDBServiceImpl;
import de.iubh.fernstudium.ticketsystem.db.util.JPAHibernateTestManager;
import de.iubh.fernstudium.ticketsystem.db.utils.CustomNativeQuery;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TicketDBServiceTest extends JPAHibernateTestManager {

    private TicketDBService ticketDBService = new TicketDBServiceImpl();
    private UserDBService userDBService = new UserDBServiceImpl();
    private CategoryDBService categoryDBService = new CategoryDBServiceImpl();

    private static boolean isDataLoaded = false;

    @Before
    public void initEntityManager(){
        Whitebox.setInternalState(ticketDBService, "em", super.em);
        Whitebox.setInternalState(userDBService, "em", super.em);
        Whitebox.setInternalState(categoryDBService, "em", super.em);

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
    public void test1CreateTicket() throws CategoryNotFoundException {
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setComments(null);
        ticketEntity.setTicketStatus(TicketStatus.NEW);
        ticketEntity.setAssignee(getUser("tutor"));
        ticketEntity.setCategory(getCategory("ISEF"));
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
        TicketEntity t = ticketDBService.addComment(1L, commentEntity);
        Assert.assertNotNull(t);
        em.getTransaction().commit();
    }

    @Test
    public void test51TicketHasComments(){
        TicketEntity t = ticketDBService.getTicketById(1L);
        Assert.assertTrue(t.getComments().size() == 1);
        Assert.assertEquals("I am a Comment", t.getComments().get(0).getComment());
    }

    @Test
    public void test60Masterticket() throws CategoryNotFoundException {
        TicketEntity t1 = new TicketEntity("Kind1", "Kind1", TicketStatus.NEW, getUser("student"),
                DateTimeUtil.now(), getCategory("ISEF"), getUser("tutor"), null, null, null);
        em.getTransaction().begin();
        TicketEntity temp = ticketDBService.createTicket(t1);
        assertNotNull(temp);

        System.out.println("TicketId: " + temp.getId());
        assertTrue(temp.getId() > 0);

        try {
            ticketDBService.createMasterTicket(1L, temp.getId());
        } catch (NoSuchTicketException e) {
            e.printStackTrace();
        }
        em.getTransaction().commit();

        TicketEntity child = ticketDBService.getTicketById(temp.getId());
        assertEquals(new Long(1), child.getMasterTicket().getId());

        TicketEntity master = ticketDBService.getTicketById(1L);
        assertTrue(master.getMasterTicket() == null);
        assertTrue(master.getChildTickets().size() == 1);
        assertTrue(master.getChildTickets().get(0).getId() == temp.getId());

        t1 = new TicketEntity("Kind2", "Kind2", TicketStatus.NEW, getUser("student"),
                DateTimeUtil.now(), getCategory("ISEF"), getUser("tutor"), null, null, null);
        TicketEntity temp1 = ticketDBService.createTicket(t1);

        try {
            ticketDBService.createMasterTicket(1L, temp1.getId());
        } catch (NoSuchTicketException e) {
            e.printStackTrace();
        }

        TicketEntity child1 = ticketDBService.getTicketById(temp1.getId());
        assertEquals(new Long(1), child1.getMasterTicket().getId());

        TicketEntity master1 = ticketDBService.getTicketById(1L);
        assertTrue(master1.getMasterTicket() == null);
        assertTrue(master1.getChildTickets().size() == 2);
        assertTrue(master1.getChildTickets().get(1).getId() == temp1.getId());

        System.out.println(master1.toDto().toString());
        System.out.println(child.toDto().toString());
        System.out.println(child1.toDto().toString());
    }

    @Test
    public void test70getTicketsForUserId(){
        List<TicketEntity> ticketEntities = ticketDBService.getTicketsForUserId(getUser("tutor"));
        assertNotNull(ticketEntities);
    }

    @Test
    public void test71getTicketsReportedByUserId(){
        List<TicketEntity> ticketEntities = ticketDBService.getTicketsReportedByUserId(getUser("tutor"));
        assertNotNull(ticketEntities);
    }

    @Test
    public void test80searchByRepoter(){
        List<TicketEntity> ticketEntities = ticketDBService.searchByReporter("tutor");
        assertNotNull(ticketEntities);
    }

    @Test
    public void test81searchByAssignee(){
        List<TicketEntity> ticketEntities = ticketDBService.searchByAssignee("tutor");
        assertNotNull(ticketEntities);
    }

    @Test
    public void test82searchByStatus(){
        List<TicketEntity> ticketEntities = ticketDBService.searchByStatus(TicketStatus.NEW);
        assertNotNull(ticketEntities);
    }

    @Test
    public void test83searchByTitle(){
        List<TicketEntity> ticketEntities = ticketDBService.searchByTitle("Testticket");
        assertNotNull(ticketEntities);
    }

    @Test
    public void test84searchByCustomQuery(){
        CustomNativeQuery q = CustomNativeQuery.builder().selectAll().from("TICKET").where("reporter_USERID").isEqualTo("tutor").buildQuery();
        List<TicketEntity> ticketEntities = ticketDBService.searchByCustomQuery(q.getQueryString(), q.getParameters());
        assertNotNull(ticketEntities);
    }


    private UserEntity getUser(String id) {
        return userDBService.findById(id);
    }


    private CategoryEntity getCategory(String id) throws CategoryNotFoundException { return categoryDBService.getCategoryById(id);
    }
}
