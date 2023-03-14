package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.Transfer_Type;
import com.techelevator.tenmo.model.Transfer_status;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.support.rowset.SqlRowSet;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> transferHistory() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM transfer;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()){
            transfers.add(mapToTransfer(results));
        }
        return transfers;
    }


    @Override
        public List<Transfer> list_transfer_by_name(String username) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT  transfer_id ,transfer_type_id, transfer_status_id, account_from, account_to, amount  FROM transfer tr " +
                "JOIN account a ON tr.account_to = a.account_id OR tr.account_from = a.account_id " +
                "JOiN tenmo_user tn on tn.user_id = a.user_id "+
                "WHERE username LIKE ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        while(results.next()){
            transfers.add(mapToTransfer(results));
        }
        return transfers;
    }

    @Override
    public List<Transfer> list_pending_transfer(String username) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT  transfer_id ,transfer_type_id, transfer_status_id, account_from, account_to, amount  FROM transfer tr " +
                "JOIN account a ON tr.account_to = a.account_id OR tr.account_from = a.account_id " +
                "JOiN tenmo_user tn on tn.user_id = a.user_id "+
                "WHERE username LIKE ? AND transfer_status_id = 1;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        while(results.next()){
            transfers.add(mapToTransfer(results));
        }
        return transfers;
    }


    @Override
    public Transfer createTransfer(Transfer transfer) {

        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount ) VALUES (?,?,?,?,? );";

         jdbcTemplate.update(sql,transfer.getTransfer_type_id(), transfer.getTransfer_status_id(),
                transfer.getAccount_from(), transfer.getAccount_to(), transfer.getAmount());
            return transfer;
    }
    @Override
    public void changeTransferStatus(Transfer transfer){
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, transfer.getTransfer_status_id(), transfer.getTransfer_id());
    }
    @Override
    public Transfer_Type getTransferTypeWithTransferTypeId(Transfer transfer) {
        Transfer_Type transfer_type = new Transfer_Type();
        String sql = "SELECT transfer_type_id, transfer_type_desc FROM transfer_type WHERE transfer_type_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transfer.getTransfer_type_id());
        if(results.next()){
            transfer_type.setTransfer_type_id(results.getInt("transfer_type_id"));
            transfer_type.setTransfer_type_desc(results.getString("transfer_type_desc"));
        }
        return transfer_type;
    }
    @Override
    public Transfer_status getTransferStatusWithTransferTypeId(Transfer transfer) {
        Transfer_status transfer_status = new Transfer_status();
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_status WHERE transfer_status_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transfer.getTransfer_status_id());
        if(results.next()){
            transfer_status.setTransfer_status_id(results.getInt("transfer_status_id"));
            transfer_status.setTransfer_status_desc(results.getString("transfer_status_desc"));
        }
        return transfer_status;
    }


    @Override
    public void withdrawTransfer(int transferId) {

    }


    @Override
    public void withdrawBalance(int transferId) {
        String sql = "";
    }
    public void depositBalance(int transferId) {
        String sql = "";
    }
 public Transfer mapToTransfer(SqlRowSet rowSet){
        Transfer transfer = new Transfer();
        transfer.setId(rowSet.getInt("transfer_id"));
        transfer.setTransfer_type_id(rowSet.getInt("transfer_type_id"));
        transfer.setTransfer_status_id(rowSet.getInt("transfer_status_id"));
        transfer.setAccount_from(rowSet.getInt("account_from"));
        transfer.setAccount_to(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getDouble("amount"));
        return transfer;
    }

}




//        String sqlGet = "SELECT transfer_id, amount FROM transfer WHERE transfer_id = ?;";
//
//        SqlRowSet results =  jdbcTemplate.queryForRowSet(sqlGet, transferId);
//        if(results.next()){
//            finalTransfer = mapToTransfer(results);
//        }


//    @Override
//    public void withdrawTransfer(int transferId) {
//        String sql = "SELECT * FROM transfer WHERE transfer_id = ?;";
//        SqlRowSet results = j
//    }

//                "JOIN transfer_type ty ON ty.transfer_type_id = t.transfer_type_id" +
//                "JOIN transfer_status ts ON ts.transfer_status_id = t.transfer_status_id" +
//                "JOIN account a ON a.user_id = t.account_from OR a.user_id = t.account_to;";
