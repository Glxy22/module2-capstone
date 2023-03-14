package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class AccountServiceDto implements AccountService{
    private final String baseUrl;
    private RestTemplate restTemplate;

    public AccountServiceDto(String baseurl){
        this.baseUrl= baseurl;
        this.restTemplate =new RestTemplate();
    }

    @Override
    public Account getBalance(AuthenticatedUser authenticatedUser) {
        HttpEntity entity = createHttpEntity(authenticatedUser);
        Account balance = null;

        try {
            balance = restTemplate.exchange(baseUrl + "/balance",
                    HttpMethod.GET,
                    entity,
                    Account.class
            ).getBody();
        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issue. Please try again.");
        }

        return balance;
    }
 // getting list of transaction for current user

    @Override
    public Transfer[] list_transaction(AuthenticatedUser authenticatedUser) {
        HttpEntity entity = createHttpEntity(authenticatedUser);
        Transfer[] transfer = new Transfer[0];
        try {
            ResponseEntity<Transfer[]> responseEntity = restTemplate.exchange(
                    baseUrl + "/list_transaction", HttpMethod.GET,entity, Transfer[].class);

            transfer = responseEntity.getBody();

        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issue. Please try again.");
        }

        return transfer;
    }
    @Override
    public Transfer[] list_pending_requests(AuthenticatedUser authenticatedUser) {
        HttpEntity entity = createHttpEntity(authenticatedUser);
        Transfer[] transfer = new Transfer[0];
        try {
            ResponseEntity<Transfer[]> responseEntity = restTemplate.exchange(
                    baseUrl + "/view_pending_requests", HttpMethod.GET,entity, Transfer[].class);

            transfer = responseEntity.getBody();

        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issue. Please try again.");
        }

        return transfer;
    }



        //transfer approved ???????????? how to handel the call in API
    @Override
     public void balanceAdjustmentAfterTransfer(AuthenticatedUser authenticatedUser,Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer,headers);


        restTemplate.exchange(baseUrl+"/transfer_approved",HttpMethod.PUT,entity,Transfer.class);

//          restTemplate.exchange(baseUrl+"/transfer_approved/",HttpMethod.PUT,entity,Transfer.class).getBody();

    }

    @Override
    public int getAccountByUserId(AuthenticatedUser authenticatedUser,int id) {
        HttpEntity<Account> entity = createHttpEntity(authenticatedUser);
        Account account = null;
        try{
        ResponseEntity<Account> response = restTemplate.exchange(baseUrl+"/account_id/"+id,HttpMethod.GET,entity,Account.class);
        account= response.getBody();
        }
        catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issue. Please try again.");
        }

        return account.getAccount_id();
    }

    public int getAccountByAccountID(AuthenticatedUser authenticatedUser, int id ){
        HttpEntity<Account> entity = createHttpEntity(authenticatedUser);
        Account account = null;
        try{
            ResponseEntity<Account> response = restTemplate.exchange(baseUrl+"/account_with_acc_id/"+id,HttpMethod.GET,entity,Account.class);
            account= response.getBody();
        }
        catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issue. Please try again.");
        }

        return account.getAccount_id();
    }

    public Account getAccToByAccountID(AuthenticatedUser authenticatedUser, int id ){
        HttpEntity<Account> entity = createHttpEntity(authenticatedUser);
        Account account = null;
        try{
            ResponseEntity<Account> response = restTemplate.exchange(baseUrl+"/account_with_acc_id/"+id,HttpMethod.GET,entity,Account.class);
            account= response.getBody();
        }
        catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issue. Please try again.");
        }

        return account;
    }

    @Override
    public int createTransfer(AuthenticatedUser authenticatedUser,Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer,headers);
        int id = authenticatedUser.getUser().getId();

        Transfer transfer1 = restTemplate.exchange(baseUrl + "/create_transfer", HttpMethod.POST, entity, Transfer.class).getBody();

        return  transfer1.getTransfer_id();
    }


    private HttpEntity createHttpEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity(httpHeaders);
        return entity;
    }
}
