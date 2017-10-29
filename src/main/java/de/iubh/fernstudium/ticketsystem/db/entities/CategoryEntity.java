package de.iubh.fernstudium.ticketsystem.db.entities;

import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;

import javax.inject.Named;
import javax.persistence.*;

@Entity
@NamedQueries(
        @NamedQuery(name = "findAllCategories", query = "select c from CategoryEntity c")
)
@Table(name = "CATEGORY")
public class CategoryEntity {

    @Id
    @Column(name = "CATEGID", nullable = false, length = 64, unique = true)
    private String categoryId;

    @Column(name = "CATEG_NAME", nullable = false, length = 254, unique = true)
    private String categoryName;

    @OneToOne(fetch = FetchType.EAGER)
    private UserEntity tutor;

    public CategoryEntity() {
    }

    public CategoryEntity(String categoryId, String categoryName, UserEntity tutor) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.tutor = tutor;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public UserEntity getTutor() {
        return tutor;
    }

    public void setTutor(UserEntity tutor) {
        this.tutor = tutor;
    }

    public CategoryDTO toDto(){
        return new CategoryDTO(categoryId, categoryName, tutor.toDto());
    }
}
