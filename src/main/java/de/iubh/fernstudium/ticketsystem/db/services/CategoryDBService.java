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
     * @throws CategoryNotFoundException
     */
    CategoryEntity getCategoryById(String categoryId) throws CategoryNotFoundException;

    /**
     * Ändert den Tutor zum einem Kurs
     *
     * @param categoryId
     * @return boolean
     * @throws CategoryNotFoundException
     */
    boolean changeTutor(String categoryId, UserEntity newTutor) throws CategoryNotFoundException;

    /**
     * Ändert einen Kursnamen zu einem Kurs
     *
     * @param courseId
     * @param newCategoryName
     * @return boolean
     * @throws CategoryNotFoundException
     */
    boolean changeCategoryName(String courseId, String newCategoryName) throws CategoryNotFoundException;

    /**
     * Ändert die Daten einer Kategorie-Definition
     *
     * @param categoryEntity
     * @return boolean
     * @throws CategoryNotFoundException
     */
    CategoryEntity changeCategory(CategoryEntity categoryEntity) throws CategoryNotFoundException;

    /**
     * Löscht einen Kurs aus dem Bestand
     *
     * @param categoryId
     * @return boolean
     * @throws CategoryNotFoundException
     */
    boolean deleteCategory(String categoryId) throws CategoryNotFoundException;

    /**
     * Fügt einen neuen Kurs hinzu
     *
     * @param categoryEntity
     * @return boolean
     */
    boolean addCategory(CategoryEntity categoryEntity);

    /**
     * Liest alle Kategorien von der DB.
     *
     * @return Liste an {@link CategoryEntity}
     */
    List<CategoryEntity> getAllCategories();

    /**
     * Liefert alle Kategorien mit angegebenem Namen
     *
     * @param categoryName
     * @return Liste an {@link CategoryEntity}
     */
    List<CategoryEntity> getCategoryByName(String categoryName);

}
