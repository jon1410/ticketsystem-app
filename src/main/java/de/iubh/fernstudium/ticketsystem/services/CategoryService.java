package de.iubh.fernstudium.ticketsystem.services;

import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;

import java.util.List;

public interface CategoryService {

    /**
     * Ermittelt die Details zu einer Kurs-ID
     *
     * @param courseId
     * @return CategoryDTO
     */
    CategoryDTO getCategoryById(String courseId) throws CategoryNotFoundException;

    /**
     * Ändert den Tutor zum einem Kurs
     *
     * @param courseId
     * @return boolean
     */
    boolean changeTutor(String courseId, String newTutor) throws CategoryNotFoundException, UserNotExistsException;

    /**
     * Ändert einen Kursnamen zu einem Kurs
     *
     * @param courseId
     * @param newCourseName
     * @return boolean
     */
    boolean changeCategoryName(String courseId, String newCourseName) throws CategoryNotFoundException;

    /**
     * Ändert die Daten der Kategorie
     *
     * @param categoryDTOs
     * @return boolean
     * @throws CategoryNotFoundException
     */
    boolean changeCategory(CategoryDTO categoryDTOs) throws CategoryNotFoundException, UserNotExistsException;

    /**
     * Löscht einen Kurs aus dem Bestand
     *
     * @param courseId
     * @return boolean
     */
    boolean deleteCategoryById(String courseId) throws CategoryNotFoundException;

    /**
     * Fügt einen neuen Kurs hinzu
     *
     * @param categoryDTO
     * @return boolean
     */
    boolean addCategory(CategoryDTO categoryDTO);

    /**
     * Liest alle definierten Kategorien
     *
     * @return Liste an {@link CategoryDTO}
     */
    List<CategoryDTO> getAllCategories();
}
