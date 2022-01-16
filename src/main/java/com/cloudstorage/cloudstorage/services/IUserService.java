package com.cloudstorage.cloudstorage.services;

import com.cloudstorage.cloudstorage.entities.Role;
import com.cloudstorage.cloudstorage.entities.User;
import com.cloudstorage.cloudstorage.models.UserRes;
import com.cloudstorage.cloudstorage.utils.exceptions.UserAlreadyExistException;
import com.cloudstorage.cloudstorage.utils.exceptions.UserNotFoundException;

import java.util.List;

public interface IUserService {
    User registration(User user) throws UserAlreadyExistException;
    Role saveRole(Role role);
    void addRoleToUser(String email, String roleName);
    User getUser(String email) throws UserNotFoundException;
    UserRes getUserRes(String email) throws UserNotFoundException;
    List<User> getUsers();
}
