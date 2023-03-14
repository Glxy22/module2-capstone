package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
@RestController
public class Controller {
    private UserDao userDao;
    private AccountDao accountDao;
    private TransferDao transferDao;

    public Controller(UserDao userDao, AccountDao accountDao, TransferDao transferDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }


    //return user by ID

    @RequestMapping(path = "user/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable int id) {
        User user = userDao.getUserById(id);
        return user;
    }

    //return list of all users
    @RequestMapping(path = "/tenmo_user", method = RequestMethod.GET)
    public List<User> list() {
        return userDao.findAll();
    }

    // return user by name
    @RequestMapping(path = "", method = RequestMethod.GET)
    public User findUser(@RequestParam String name) {
        User user = userDao.findByUsername(name);
        return user;
    }

    // creating transfer request
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/create_transfer", method = RequestMethod.POST)
    public Transfer createTransfer(@RequestBody Transfer transfer) {
        Transfer transfer1=null;
        transfer1= transferDao.createTransfer(transfer);

        System.out.println(transfer.getTransfer_id());
        return transfer1;
    }
    @RequestMapping(path = "/view_pending_requests", method = RequestMethod.GET)
    public List<Transfer> viewPendingRequests(Principal principal) {
        List<Transfer> transfers = null;
        transfers = transferDao.list_pending_transfer(principal.getName());

        return transfers;
    }

//returning transaction for current user

    @RequestMapping(path = "/list_transaction", method = RequestMethod.GET)
    public List<Transfer> listTransfers(Principal principal) {
        List<Transfer> transfers = null;
        transfers = transferDao.list_transfer_by_name(principal.getName());

        return transfers;
    }

    @RequestMapping(path = "/pending_transfer_status_change", method = RequestMethod.PUT)
    public void changeTransferStatus(@RequestBody Transfer transfer){

        transferDao.changeTransferStatus(transfer);
    }

    @RequestMapping(path = "/user_account/{id}", method = RequestMethod.GET)
    public User getUsernameById(@PathVariable int id){
        User user = userDao.getUserByAccountId(id);
        if(user == null){

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user Not Found");
        } else {
        return user;
        }
    }
    @RequestMapping(path = "/transfer_type", method = RequestMethod.POST)
    public Transfer_Type getTransferTypeWithTransferTypeId(@RequestBody Transfer transfer){
        Transfer_Type transfer_type = transferDao.getTransferTypeWithTransferTypeId(transfer);

        if(transfer_type == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user Not Found");
        } else {
            return transfer_type;
        }
    }
    @RequestMapping(path = "/transfer_status", method = RequestMethod.POST)
    public Transfer_status getTransferStatusWithTransferTypeId(@RequestBody Transfer transfer){
        Transfer_status transfer_status = transferDao.getTransferStatusWithTransferTypeId(transfer);

        if(transfer_status == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user Not Found");
        } else {
            return transfer_status;
        }
    }
//    @RequestMapping(path = "/withdrawTransfer/{id}", method = RequestMethod.PUT)
//    public void withdrawTransfer(@PathVariable int id) {
//        Transfer transfer1 = new Transfer();
//        double amount = 200;
//        Account account = null;
//        account = accountDao.withdrawBalance(id, amount);
//    }
}

