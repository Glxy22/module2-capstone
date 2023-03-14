package com.techelevator.tenmo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.tenmo.model.*;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService(String url) {
        this.baseUrl = url;
    }

    public Transfer getTransfer(AuthenticatedUser authenticatedUser, Transfer transfer){
        HttpEntity<Transfer> entity = createTransferEntity(authenticatedUser, transfer);

        try{
            String serializedTransferObject = new ObjectMapper().writeValueAsString(transfer);
            restTemplate.put(baseUrl + "/pending_transfer_status_change", entity);

        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return transfer;
    }

    public boolean pendingTransferStatusChange(AuthenticatedUser authenticatedUser, Transfer transfer){
        HttpEntity<Transfer> entity = createTransferEntity(authenticatedUser, transfer);
        boolean success = false;
        try{
            String serializedTransferObject = new ObjectMapper().writeValueAsString(transfer);
            restTemplate.put(baseUrl + "/pending_transfer_status_change", entity);
            success = true;

        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return success;
    }
    public Transfer_Type getTransferTypeWithTransferTypeId(AuthenticatedUser authenticatedUser, Transfer transfer ){
        Transfer_Type transfer_type = null;
        HttpEntity<Transfer> entity = createTransferEntity(authenticatedUser, transfer);

        try {
            // Create a Transfer_Type object with the necessary data
            Transfer_Type transferType = new Transfer_Type();
            transferType.setTransfer_type_id(transfer.getTransfer_type_id());

            // Send a POST request to the API endpoint with the Transfer_Type object in the request body
            ResponseEntity<Transfer_Type> response = restTemplate.exchange(
                    baseUrl + "/transfer_type",
                    HttpMethod.POST,
                    new HttpEntity<>(transferType, entity.getHeaders()),
                    Transfer_Type.class
            );

            transfer_type = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer_type;
    }
    public Transfer_status getTransferStatusWithTransferTypeId(AuthenticatedUser authenticatedUser, Transfer transfer ){
        Transfer_status transfer_status = null;
        HttpEntity<Transfer> entity = createTransferEntity(authenticatedUser, transfer);

        try {
            // Create a Transfer_Type object with the necessary data
            Transfer_status transfer_Status = new Transfer_status();
            transfer_Status.setTransfer_status_id(transfer.getTransfer_type_id());

            // Send a POST request to the API endpoint with the Transfer_Type object in the request body
            ResponseEntity<Transfer_status> response = restTemplate.exchange(
                    baseUrl + "/transfer_status",
                    HttpMethod.POST,
                    new HttpEntity<>(transfer_Status, entity.getHeaders()),
                    Transfer_status.class
            );

            transfer_status = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer_status;
    }
    private HttpEntity<Transfer> createTransferEntity(AuthenticatedUser authenticatedUser, Transfer transfer) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(transfer, headers);
    }

}
