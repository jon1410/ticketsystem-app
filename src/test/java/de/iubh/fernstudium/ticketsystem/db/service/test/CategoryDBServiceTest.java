package de.iubh.fernstudium.ticketsystem.db.service.test;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;
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
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
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
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryDBServiceTest extends JPAHibernateTestManager {

    private CategoryDBService categoryDBService = new CategoryDBServiceImpl();

    private static boolean isDataLoaded = false;

    @Before
    public void initEm(){
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
    public void test1GetCategoryById() throws CategoryNotFoundException {
        CategoryEntity categoryEntity = categoryDBService.getCategoryById("ISEF");
        assertNotNull(categoryEntity);
    }

    @Test(expected = CategoryNotFoundException.class)
    public void test2GetCategoryByIdException() throws CategoryNotFoundException {
        CategoryEntity categoryEntity = categoryDBService.getCategoryById("XY");
    }

    @Test
    public void test3ChangeTutor() throws CategoryNotFoundException {
        UserEntity newTutor = buildTutor();
        assertTrue(categoryDBService.changeTutor("ISEF", newTutor));
    }

    @Test
    public void test4ChangeCategoryName() throws CategoryNotFoundException {
        assertTrue(categoryDBService.changeCategoryName("ISEF", "newName"));
    }

    @Test
    public void test5ChangeCategory() throws CategoryNotFoundException {
        CategoryEntity categoryEntity = new CategoryEntity("ISEF", "newName1", buildTutor());
        CategoryEntity categoryEntity1 = categoryDBService.changeCategory(categoryEntity);
        assertNotNull(categoryEntity1);
    }

    @Test
    public void test6DeleteCategory() throws CategoryNotFoundException {
        em.getTransaction().begin();
        assertTrue(categoryDBService.deleteCategory("ISEF"));
        em.getTransaction().commit();
    }

    @Test
    public void test7AddCategory(){
        CategoryEntity categoryEntity = new CategoryEntity("ISEF", "newName1", buildTutor());
        em.getTransaction().begin();
        assertTrue(categoryDBService.addCategory(categoryEntity));
        em.getTransaction().commit();
    }

    @Test
    public void test8GetAllCategories(){
        List<CategoryEntity> categoryEntities = categoryDBService.getAllCategories();
        assertNotNull(categoryEntities);
        assertTrue(categoryEntities.size() == 1);
    }

    private UserEntity buildTutor() {
        return new UserEntity("tutor1", "tutor1", "tutor1", "tutor1", UserRole.TU);
    }
}
