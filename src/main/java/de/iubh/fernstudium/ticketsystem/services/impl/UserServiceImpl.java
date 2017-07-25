package de.iubh.fernstudium.ticketsystem.services.impl;

import de.iubh.fernstudium.ticketsystem.db.services.UserDBService;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class UserServiceImpl implements UserService {

    @Inject
    private UserDBService userDBService;

    @Override
    public UserDTO getUserByUserId(String userId) throws UserNotExistsException {
        return null;
    }

    @Override
    public boolean createUser(String userId, String firstName, String lastName, String password, UserRole role, String mailAdress) throws UserAlreadyExistsException {
        return false;
    }

    @Override
    public boolean login(String userId, String password) throws UserNotExistsException, InvalidPasswordException {
        return false;
    }

    @Override
    public boolean changePassword(String userId, String altesPw, String neuesPw) throws UserNotExistsException, InvalidPasswordException {
        return false;
    }
}
