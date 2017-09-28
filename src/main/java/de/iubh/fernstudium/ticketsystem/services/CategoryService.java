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
    public CategoryDTO getCourseById(String courseId) throws CategoryNotFoundException;

    /**
     * Ändert den Tutor zum einem Kurs
     *
     * @param courseId
     * @return boolean
     */
    public boolean changeTutor(String courseId, String newTutor) throws CategoryNotFoundException, UserNotExistsException;

    /**
     * Ändert einen Kursnamen zu einem Kurs
     *
     * @param courseId
     * @param newCourseName
     * @return boolean
     */
    public boolean changeCourseName(String courseId, String newCourseName) throws CategoryNotFoundException;

    /**
     * Löscht einen Kurs aus dem Bestand
     *
     * @param courseId
     * @return boolean
     */
    public boolean deleteCourseById(String courseId) throws CategoryNotFoundException;

    /**
     * Fügt einen neuen Kurs hinzu
     *
     * @param categoryDTO
     * @return boolean
     */
    public boolean addCourse(CategoryDTO categoryDTO);

    /**
     * Liest alle definierten Kategorien
     *
     * @return Liste an {@link CategoryDTO}
     */
    public List<CategoryDTO> getAllCategories();
}
