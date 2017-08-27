package de.iubh.fernstudium.ticketsystem.dtos;

import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;

/**
 * Created by ivanj on 04.07.2017.
 */
public class UserDTO {

    private String userId;
    private String firstName;
    private String lastName;
    private String password;
    private UserRole role;

    public UserDTO() {
    }

    public UserDTO(String userId, String firstName, String lastName, String password, UserRole role) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role.getResolvedRoleText();
    }

    public void setRole(String role) {
        this.role = UserRole.valueOf(role);
    }

    public UserRole getUserRole(){ return role; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        if (userId != null ? !userId.equals(userDTO.userId) : userDTO.userId != null) return false;
        if (firstName != null ? !firstName.equals(userDTO.firstName) : userDTO.firstName != null) return false;
        if (lastName != null ? !lastName.equals(userDTO.lastName) : userDTO.lastName != null) return false;
        if (password != null ? !password.equals(userDTO.password) : userDTO.password != null) return false;
        return role == userDTO.role;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    /**
     * Erzeugt die entsprechende Entity zum DTO
     * @return UserEntity
     */
    public UserEntity toEntity(){
        return new UserEntity(userId, firstName, lastName, password, role);
    }
}
