package de.iubh.fernstudium.ticketsystem.db.services;

import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;

public interface UserDBService {

    /**
     * Erzeugt einen neuen User in der DB:
     *
     * @param userEntity
     */
    public void persistUser(UserEntity userEntity);

    /**
     * Ändert das Passwort des Users
     *
     * @param userId, newPassword
     */
    public void changePassword(String userId, String newPassword);

    /**
     * Liest einen User von der Datenbank mittels eindeutiger ID.
     *
     * @param id
     * @return UserEntity
     */
    public UserEntity findById(String id);

    /**
     * Löscht einen User von der Datenbank über seine eindeutige UserID
     *
     * @param userId
     */
    public void deleteUser(String userId);

    /**
     * Ändert die Daten eines Users
     *
     * @param userEntity
     */
    public void updateUser(String UserId, String firstName, String lastName, UserRole newRole);
}
