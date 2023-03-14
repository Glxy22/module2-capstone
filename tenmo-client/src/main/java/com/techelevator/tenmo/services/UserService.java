package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;

public interface UserService {
    User[] getAllUsers(AuthenticatedUser authenticatedUser);
    User getUserByAccountID(AuthenticatedUser authenticatedUser, int id );
}
