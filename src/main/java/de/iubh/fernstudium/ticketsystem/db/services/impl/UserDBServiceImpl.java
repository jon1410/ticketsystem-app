package de.iubh.fernstudium.ticketsystem.db.services.impl;

import de.iubh.fernstudium.ticketsystem.beans.LoginBean;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.UserDBService;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtil;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtilImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.registry.infomodel.User;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Stateless
public class UserDBServiceImpl implements UserDBService{

    @PersistenceContext
    private EntityManager em;

    @Override
    public void persistUser(UserEntity userEntity) {
        em.persist(userEntity);
    }

    @Override
    public void changePassword(String userId, String newPassword) {
        UserEntity userEntity = this.findById(userId);
        userEntity.setPassword(newPassword);
    }

    @Override
    public UserEntity findById(String id) {
        return em.find(UserEntity.class, id);
    }

    @Override
    public void deleteUser(String userId) {
        em.remove(this.findById(userId));
    }

    @Override
    public void updateUser(String userId, String firstName, String lastName, UserRole newRole) {
        UserEntity userEntity = this.findById(userId);
        if(firstName != null){
            userEntity.setFirstName(firstName);
        }
        if(lastName != null){
            userEntity.setLastName(lastName);
        }
        if(newRole != null){
            userEntity.setRole(newRole);
        }
    }

    @Override
    public List<UserEntity> findByRole(UserRole role) {
        Query query = em.createNamedQuery("findByRole", UserEntity.class).setParameter("role", role);
        return query.getResultList();
    }
}
