package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface AccountDao {

    Account getBalance(String name);
    void transferFunds(Transfer transfer);
    Account getAccountById(int id);
    Account getAccountByUserID(int userId);
    Account  getAccountByAccountID(int account_Id);
//    Transfer transferDetails(int id);


}