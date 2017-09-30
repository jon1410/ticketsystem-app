package de.iubh.fernstudium.ticketsystem.dtos;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;

public class CategoryDTO {

    private String courseId;
    private String courseName;
    private UserDTO tutor;

    public CategoryDTO() {
    }

    public CategoryDTO(String courseId, String courseName, UserDTO tutor) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.tutor = tutor;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public UserDTO getTutor() {
        return tutor;
    }

    public void setTutor(UserDTO tutor) {
        this.tutor = tutor;
    }

    public CategoryEntity toEntity(){
        return new CategoryEntity(courseId, courseName, tutor.toEntity());
    }
}
