package de.iubh.fernstudium.ticketsystem.db.services;

import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;

import java.util.List;

public interface UserDBService {

    /**
     * Erzeugt einen neuen User in der DB:
     *
     * @param userEntity
     */
    void persistUser(UserEntity userEntity);

    /**
     * Ändert das Passwort des Users
     *
     * @param userId, newPassword
     */
    void changePassword(String userId, String newPassword);

    /**
     * Liest einen User von der Datenbank mittels eindeutiger ID.
     *
     * @param id
     * @return UserEntity
     */
    UserEntity findById(String id);

    /**
     * Löscht einen User von der Datenbank über seine eindeutige UserID
     *
     * @param userId
     */
    void deleteUser(String userId);

    /**
     * Ändert die Daten eines Users
     *
     * @param userId, firstName, lastName, newRole
     */
    void updateUser(String userId, String firstName, String lastName, UserRole newRole);

    /**
     * Liest alle Benutzer mit der angebeben Rollen
     *
     * @param role
     * @return Liste von {@link UserEntity}
     */
    List<UserEntity> findByRole(UserRole role);
}
