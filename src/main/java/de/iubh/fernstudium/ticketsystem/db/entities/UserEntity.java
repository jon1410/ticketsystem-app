package de.iubh.fernstudium.ticketsystem.db.entities;

import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;

import javax.persistence.*;

/**
 * Created by ivanj on 16.07.2017.
 */
@Entity
@Table(name = "TICKET_USER", indexes = {@Index(name="IDX_ROLE", columnList = "ROLE")})
@NamedQueries({
        @NamedQuery(name = "findByRole", query = "select u from UserEntity u where u.role = :role ")
})
public class UserEntity {

    @Id
    @Column(name = "USERID", nullable = false, length = 64, unique = true)
    private String userId;

    @Column(name = "FIRST_NAME", nullable = false, length = 64)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 64)
    private String lastName;

    @Column(name = "PW", nullable = false)
    private String password;

    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public UserEntity() {
    }

    public UserEntity(String userId, String firstName, String lastName, String password, UserRole role) {
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserDTO toDto(){
        return new UserDTO(userId, firstName, lastName, password, role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        return role == that.role;
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
        return "UserEntity{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pw='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
