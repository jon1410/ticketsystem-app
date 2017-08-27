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
import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
}
