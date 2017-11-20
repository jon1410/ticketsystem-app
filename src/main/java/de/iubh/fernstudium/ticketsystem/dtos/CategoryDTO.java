package de.iubh.fernstudium.ticketsystem.dtos;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;

import java.io.Serializable;

public class CategoryDTO implements Serializable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryDTO that = (CategoryDTO) o;

        if (categoryId != null ? !categoryId.equals(that.categoryId) : that.categoryId != null) return false;
        if (categoryName != null ? !categoryName.equals(that.categoryName) : that.categoryName != null) return false;
        return tutor != null ? tutor.equals(that.tutor) : that.tutor == null;
    }

    @Override
    public int hashCode() {
        int result = categoryId != null ? categoryId.hashCode() : 0;
        result = 31 * result + (categoryName != null ? categoryName.hashCode() : 0);
        result = 31 * result + (tutor != null ? tutor.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", tutor=" + tutor +
                '}';
    }
}
