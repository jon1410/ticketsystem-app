package de.iubh.fernstudium.ticketsystem.db.services;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;

import java.util.List;

public interface CategoryDBService {

    /**
     * Ermittelt die Details zu einer Kurs-ID
     *
     * @param categoryId
     * @return CategoryEntity
     */
    public CategoryEntity getCategoryById(String categoryId) throws CategoryNotFoundException;

    /**
     * Ändert den Tutor zum einem Kurs
     *
     * @param categoryId
     * @return boolean
     */
    public boolean changeTutor(String categoryId, UserEntity newTutor) throws CategoryNotFoundException;

    /**
     * Ändert einen Kursnamen zu einem Kurs
     *
     * @param courseId
     * @param newCategoryName
     * @return boolean
     */
    public boolean changeCategoryName(String courseId, String newCategoryName) throws CategoryNotFoundException;

    /**
     * Ändert die Daten einer Kategorie-Definition
     *
     * @param categoryEntity
     * @return boolean
     * @throws CategoryNotFoundException
     */
    public boolean changeCategory(CategoryEntity categoryEntity) throws CategoryNotFoundException;

    /**
     * Löscht einen Kurs aus dem Bestand
     *
     * @param categoryId
     * @return boolean
     */
    public boolean deleteCategory(String categoryId) throws CategoryNotFoundException;

    /**
     * Fügt einen neuen Kurs hinzu
     *
     * @param categoryEntity
     * @return boolean
     */
    public boolean addCategory(CategoryEntity categoryEntity);

    /**
     * Liest alle Kategorien von der DB.
     *
     * @return Liste an {@link CategoryEntity}
     */
    public List<CategoryEntity> getAllCategories();

}
