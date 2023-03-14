package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    //get account by accunt ID
    @Override
    public Account getAccountById(int id){
        Account account= null;
        String sql = "select * from account " +
                "where account_id =? ;";
        SqlRowSet resource= jdbcTemplate.queryForRowSet(sql,id);
        while (resource.next()){
            account = mapToAccount(resource);
        }
        return account;
    }

    @Override
    public Account getAccountByUserID(int userId) {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        Account account = null;
        if(result.next()) {
            account = mapToAccount(result);
        }
        return account;
    }

    @Override
    public Account  getAccountByAccountID(int account_Id) {
        String sql = "SELECT * from account WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, account_Id);
        Account account = null;
        while(result.next()) {
            account = mapToAccount(result);
        }
        return account;
    }


    // get balance by user name
    @Override
    public Account getBalance(String name){
        Account account= new Account();
        String sql = "SELECT balance FROM account a "+
                "JOIN tenmo_user tu ON a.user_id = tu.user_id WHERE username = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, name);
        if(results.next()){
            double s=  results.getDouble("balance");
            account.setBalance(s);
        }
        return account;
    }


    // updating balance after transaction
    @Override
    public void transferFunds(Transfer transfer){
        Account account;
        BigDecimal d_from_bal= new BigDecimal("0.00");
        BigDecimal d_to_bal= new BigDecimal("0.00");
        BigDecimal amount_to_transfer= new BigDecimal(transfer.getAmount());
        double balance_to = 0.0, balance_from =0.0;

        String sql = "select * from account " +
                "where account_id =?;";
        SqlRowSet resource=jdbcTemplate.queryForRowSet(sql,transfer.getAccount_from());
        while(resource.next()){
            balance_from= resource.getDouble("balance");
            d_from_bal= BigDecimal.valueOf(balance_from);
        }

        SqlRowSet resource2=jdbcTemplate.queryForRowSet(sql,transfer.getAccount_to());
        while(resource2.next()){
            balance_to= resource2.getDouble("balance");
            d_to_bal= BigDecimal.valueOf(balance_to);
        }

       //subtract and add on Big decimal to achieve precise results

        d_from_bal= d_from_bal.subtract(amount_to_transfer);
        d_to_bal = d_to_bal.add(amount_to_transfer);

        //  give Big decimal back to double variables
        balance_from=d_from_bal.doubleValue();
        balance_to= d_to_bal.doubleValue();

        // update accounts balance

        String sql2 = "UPDATE account SET balance=? " +
                "where account_id =?;";
        jdbcTemplate.update(sql2,balance_from,transfer.getAccount_from());
        jdbcTemplate.update(sql2,balance_to,transfer.getAccount_to());

        String updatetransfer_status= "update transfer set transfer_status_id=2 where transfer_id=? ;";
        jdbcTemplate.update(updatetransfer_status,transfer.getTransfer_id());

    }

    public Account mapToAccount(SqlRowSet rowSet){
        Account account = new Account();
        account.setAccount_id(rowSet.getInt("account_id"));
        account.setUser_id(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getDouble("balance"));
        return account;
    }


//   public Transfer transferDetails(int id){
//        String sql= "select tr"
//    }


}



