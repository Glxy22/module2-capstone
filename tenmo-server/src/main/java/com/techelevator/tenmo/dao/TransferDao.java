package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.Transfer_Type;
import com.techelevator.tenmo.model.Transfer_status;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TransferDao {
    public List<Transfer> transferHistory();

    public List<Transfer> list_transfer_by_name(String username);

    public Transfer createTransfer(Transfer transfer);
    public void withdrawTransfer(int transferId);

    public void withdrawBalance(int transferId);
    public List<Transfer> list_pending_transfer(String username);
    public void changeTransferStatus(Transfer transfer);
    public Transfer_Type getTransferTypeWithTransferTypeId(Transfer transfer);
    public Transfer_status getTransferStatusWithTransferTypeId(Transfer transfer);
}
