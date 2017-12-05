package de.iubh.fernstudium.ticketsystem.db.service.test;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.HistoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.CategoryDBService;
import de.iubh.fernstudium.ticketsystem.db.services.HistoryDBService;
import de.iubh.fernstudium.ticketsystem.db.services.TicketDBService;
import de.iubh.fernstudium.ticketsystem.db.services.UserDBService;
import de.iubh.fernstudium.ticketsystem.db.services.impl.CategoryDBServiceImpl;
import de.iubh.fernstudium.ticketsystem.db.services.impl.HistoryDBServiceImpl;
import de.iubh.fernstudium.ticketsystem.db.services.impl.TicketDBServiceImpl;
import de.iubh.fernstudium.ticketsystem.db.services.impl.UserDBServiceImpl;
import de.iubh.fernstudium.ticketsystem.db.util.JPAHibernateTestManager;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;
import org.h2.tools.RunScript;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.powermock.reflect.Whitebox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HistoryDBServiceTest extends JPAHibernateTestManager {

    private HistoryDBService historyDBService = new HistoryDBServiceImpl();
    private UserDBService userDBService = new UserDBServiceImpl();
    private CategoryDBService categoryDBService = new CategoryDBServiceImpl();
    private TicketDBService ticketDBService = new TicketDBServiceImpl();

    private static boolean isDataLoaded = false;

    @Before
    public void initEm(){
        //initilisiert die Entitmanager-Klasse in den DB-Serivices, baut den
        //Datenbestand neu auf (nur beim ersten initEm())
        Whitebox.setInternalState(historyDBService, "em", super.em);
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
            em.getTransaction().begin();
            ticketDBService.createTicket(createTicket());
            em.getTransaction().commit();
            isDataLoaded = true;
        }

    }

    @Test
    public void test1CreateHistoryEntry(){
        em.getTransaction().begin();
        HistoryEntity historyEntity = new HistoryEntity(getTicket(1L), DateTimeUtil.now(), HistoryAction.CA, "detail", getUser("tutor"));
        historyDBService.createHistoryEntry(historyEntity);
        em.getTransaction().commit();
    }

    @Test
    public void test2GetHistoryForTicket(){
        em.getTransaction().begin();

        List<HistoryEntity> historyEntityList = historyDBService.getHistoryForTicketId(1L);
        assertNotNull(historyEntityList);
        assertTrue(historyEntityList.size() == 1);
    }

    private TicketEntity createTicket() {
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setComments(null);
        ticketEntity.setTicketStatus(TicketStatus.NEW);
        ticketEntity.setAssignee(getUser("tutor"));

        try {
            ticketEntity.setCategory(getCategory("ISEF"));
        } catch (CategoryNotFoundException e) {
            e.printStackTrace();
        }

        ticketEntity.setCreationTime(DateTimeUtil.localDtToSqlTimestamp(LocalDateTime.now()));
        ticketEntity.setDescription("Ein Testticket");
        ticketEntity.setReporter(getUser("student"));
        ticketEntity.setTitle("Testticket");
        return ticketEntity;
    }

    private TicketEntity getTicket(Long id) {
        return ticketDBService.getTicketById(id);
    }

    private UserEntity getUser(String id) {
        return userDBService.findById(id);
    }

    private CategoryEntity getCategory(String id) throws CategoryNotFoundException { return categoryDBService.getCategoryById(id);
    }

}
