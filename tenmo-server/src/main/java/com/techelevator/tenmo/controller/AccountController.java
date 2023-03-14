package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

//import static jdk.internal.org.jline.utils.Colors.s;

@RestController
public class AccountController {
    private AccountDao accountDao;
    private TransferDao transferDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }


    //get balance........working
    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public Account getBalance(Principal principal) {
        return accountDao.getBalance(principal.getName());
    }


    //transfer amount and adjust balance accordingly.......working

    @RequestMapping(path = "/transfer_approved", method = RequestMethod.PUT)
    public void balanceAdjustmentAfterTransfer(@RequestBody Transfer transfer) {

        accountDao.transferFunds(transfer);

    }

    @RequestMapping(path = "/transfer_approved/{id}", method = RequestMethod.GET)
    public Account getAccountById(@PathVariable int id) {
        Account account;
        account = accountDao.getAccountById(id);
        return account;

    }

    @RequestMapping(path = "/account_id/{id}", method = RequestMethod.GET)
    public Account getAccountToById(@PathVariable int id) {
        Account account;
        account = accountDao.getAccountByUserID(id);
        System.out.println(account.getAccount_id());
        return account;

    }

    @RequestMapping(path = "/account_with_acc_id/{id}", method = RequestMethod.GET)
    public Account getAccountByAccountID(@PathVariable int id) {
        Account account;
        account = accountDao.getAccountByAccountID(id);
        System.out.println(account.getAccount_id());
        return account;

    }
}





//    // creating transfer request
//    @ResponseStatus(HttpStatus.CREATED)
//    @RequestMapping(path = "/transfer_approved", method = RequestMethod.POST)
//    public void createTransfer(@RequestBody Transfer transfer,Principal principal) {
//        int transfer_id = transferDao.createTransfer(transfer);
//            accountDao.transferFunds(transfer);
//        System.out.println(accountDao.getBalance(principal.getName()));
//
//    }


