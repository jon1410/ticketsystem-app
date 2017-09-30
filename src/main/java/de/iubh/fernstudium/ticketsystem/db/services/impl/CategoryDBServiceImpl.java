package de.iubh.fernstudium.ticketsystem.db.services.impl;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.CategoryDBService;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CategoryDBServiceImpl implements CategoryDBService {

    @PersistenceContext
    private EntityManager em;

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
    public boolean deleteCategory(String categoryId) throws CategoryNotFoundException {
        CategoryEntity c = this.getCategoryById(categoryId);
        em.remove(c);
        return true;
    }

    @Override
    public boolean addCategory(CategoryEntity categoryEntity) {
        em.persist(categoryEntity);
        return true;
    }

}
