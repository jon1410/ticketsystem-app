package de.iubh.fernstudium.ticketsystem.db.services.impl;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.CategoryDBService;
import de.iubh.fernstudium.ticketsystem.db.services.TicketDBService;
import de.iubh.fernstudium.ticketsystem.db.utils.CustomNativeQuery;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class CategoryDBServiceImpl implements CategoryDBService {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private TicketDBService ticketDBService;

    @Override
    public CategoryEntity getCategoryById(String categoryId) throws CategoryNotFoundException {
        CategoryEntity c = em.find(CategoryEntity.class, categoryId);
        if(c == null){
            throw new CategoryNotFoundException("Kategorie mit ID: " + categoryId + " nicht gefunden!");
        }
        return c;
    }

    @Override
    public boolean changeTutor(String categoryId, UserEntity newTutor) throws CategoryNotFoundException {
        CategoryEntity c = this.getCategoryById(categoryId);
        c.setTutor(newTutor);
        return true;
    }

    @Override
    public boolean changeCategoryName(String categoryId, String newCategoryName) throws CategoryNotFoundException {
        CategoryEntity c = this.getCategoryById(categoryId);
        c.setCategoryName(newCategoryName);
        return true;
    }

    @Override
    public CategoryEntity changeCategory(CategoryEntity categoryEntity) throws CategoryNotFoundException {
        CategoryEntity c = this.getCategoryById(categoryEntity.getCategoryId());
        c.setCategoryName(categoryEntity.getCategoryName());
        c.setTutor(categoryEntity.getTutor());
        return c;
    }

    @Override
    public boolean deleteCategory(String categoryId) throws CategoryNotFoundException {
        CategoryEntity c = this.getCategoryById(categoryId);

        CustomNativeQuery query = CustomNativeQuery.builder()
                .selectAll().from("TICKET").where("category_CATEGID").isEqualTo(c.getCategoryId()).buildQuery();

        List<TicketEntity> ticketEntityList = ticketDBService.searchByCustomQuery(query.getQueryString(), query.getParameters());
        if(CollectionUtils.isNotEmpty(ticketEntityList)){
            return false;
        }

        em.remove(c);
        return true;
    }

    @Override
    public boolean addCategory(CategoryEntity categoryEntity) {
        em.persist(categoryEntity);
        return true;
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        Query q = em.createNamedQuery("findAllCategories", CategoryEntity.class);
        return q.getResultList();
    }

    @Override
    public List<CategoryEntity> getCategoryByName(String categoryName) {
        Query q = em.createNamedQuery("findCategoryByName", CategoryEntity.class)
                .setParameter("categoryName", categoryName);
        return q.getResultList();
    }

}
