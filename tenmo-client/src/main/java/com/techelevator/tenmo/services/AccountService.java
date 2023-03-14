package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;

public interface AccountService {
    Account getBalance(AuthenticatedUser authenticatedUser);
    Transfer[] list_transaction(AuthenticatedUser authenticatedUser);
    Transfer[] list_pending_requests(AuthenticatedUser authenticatedUser);
    int getAccountByUserId(AuthenticatedUser authenticatedUser,int id);
    int createTransfer(AuthenticatedUser authenticatedUser,Transfer transfer);
    int getAccountByAccountID(AuthenticatedUser authenticatedUser, int id );
    Account getAccToByAccountID(AuthenticatedUser authenticatedUser, int id );
    void balanceAdjustmentAfterTransfer(AuthenticatedUser authenticatedUser,Transfer transfer);
}
