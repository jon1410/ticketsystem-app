package de.iubh.fernstudium.ticketsystem.db.services.impl;

import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.UserDBService;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
