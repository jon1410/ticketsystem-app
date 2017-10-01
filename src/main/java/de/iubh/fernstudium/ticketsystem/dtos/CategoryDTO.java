package de.iubh.fernstudium.ticketsystem.dtos;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;

public class CategoryDTO {

    private String categoryId;
    private String categoryName;
    private UserDTO tutor;

    public CategoryDTO() {
    }

    public CategoryDTO(String courseId, String courseName, UserDTO tutor) {
        this.categoryId = courseId;
        this.categoryName = courseName;
        this.tutor = tutor;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public UserDTO getTutor() {
        return tutor;
    }

    public void setTutor(UserDTO tutor) {
        this.tutor = tutor;
    }

    public CategoryEntity toEntity(){
        return new CategoryEntity(categoryId, categoryName, tutor.toEntity());
    }
}
