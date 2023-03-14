package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class UserServiceDto implements UserService {
    private final String API_BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();

    public UserServiceDto(String url) {
        this.API_BASE_URL = url;
    }

    //get user balance by Id ???????

    public BigDecimal getUserBalancebyid(AuthenticatedUser authenticatedUser) {
        BigDecimal balance = null;

        try {
            ResponseEntity<BigDecimal> response =
                    restTemplate.getForEntity(API_BASE_URL +"/balance", BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }
    public String getUserById (AuthenticatedUser authenticatedUser, int id ){
        User user = null;
        try {
            ResponseEntity<User> response =
                    restTemplate.exchange(API_BASE_URL + "/user_account/"+id, HttpMethod.GET, createHttpEntity(authenticatedUser), User.class);
            user = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user.getUsername();
    }


    public User[] getAllUsers(AuthenticatedUser authenticatedUser) {
        HttpEntity entity = createHttpEntity(authenticatedUser);
        User[] users = null;
        try {
            users = restTemplate.exchange(API_BASE_URL + "/tenmo_user", HttpMethod.GET, entity, User[].class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issue. Please try again.");
        }
        return users;

    }
    public User getUserByAccountID(AuthenticatedUser authenticatedUser, int id ){
        HttpEntity<User> entity = createHttpEntity(authenticatedUser);
       User user = null;
        try{
            ResponseEntity<User> response = restTemplate.exchange(API_BASE_URL+"/user_account/"+id,HttpMethod.GET,entity,User.class);
            user= response.getBody();
        }
        catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issue. Please try again.");
        }

        return user;
    }


    private HttpEntity createHttpEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity(httpHeaders);
        return entity;
    }

}

