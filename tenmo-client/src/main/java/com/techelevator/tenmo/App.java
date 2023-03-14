package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final UserServiceDto userService = new UserServiceDto(API_BASE_URL);
    //added on 3/8
    private AccountService accountService = new AccountServiceDto(API_BASE_URL);
    ;
    private TransferService transferService = new TransferService(API_BASE_URL);
    private AuthenticatedUser currentUser;


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        // TODO Auto-generated method stub
        Account balance = accountService.getBalance(currentUser);
        System.out.println("Your current account balance is:  $" + balance.getBalance());

    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub
        Transfer[] transfers = null;

        transfers = accountService.list_transaction(currentUser);
        System.out.println("------------------------------------------ ");
        System.out.println("Transfers ");
        System.out.println("ID           From/To                Amount ");
        System.out.println("------------------------------------------ ");
        for (int i = 0; i < transfers.length; i++) {
            //get from and to names and refactor the string output to have consistent spacing
            System.out.println(transfers[i].getTransfer_id() + " ACC from..... " + transfers[i].getAccount_from() +
                    " Account to..... " + transfers[i].getAccount_to() + " Amount ..." + transfers[i].getAmount());
        }
        //get user input and if user input == 0, quit
        int transferId = consoleService.promptForViewingDetailsOfTransfer();
        boolean contains = false;
        if (transferId != 0) {
            for (int i = 0; i < transfers.length; i++) {
                if (transfers[i].getTransfer_id() == transferId) {
                    System.out.println();
                    System.out.println("------------------------------------------ ");
                    System.out.println("Transfer Details ");
                    System.out.println("------------------------------------------ ");
                    System.out.println("Id: " + transfers[i].getTransfer_id());
                    System.out.println("From: " + userService.getUserById(currentUser, transfers[i].getAccount_from()));
                    System.out.println("To: " + userService.getUserById(currentUser, transfers[i].getAccount_to()));
                    System.out.println("Type: " + transferService.getTransferTypeWithTransferTypeId(currentUser, transfers[i]).getTransfer_type_desc());
                    System.out.println("Status: " + transferService.getTransferStatusWithTransferTypeId(currentUser, transfers[i]).getTransfer_status_desc());
                    System.out.println("Amount: " + transfers[i].getAmount());
                    System.out.println();
                }
            }
        } else {
            System.out.println("Invalid Selection");
        }
    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub
        Transfer[] transfers = null;

        transfers = accountService.list_pending_requests(currentUser);
        System.out.println("------------------------------------------ ");
        System.out.println("Pending Transfers ");
        System.out.println("ID           To                     Amount ");
        System.out.println("------------------------------------------ ");
        for (int i = 0; i < transfers.length; i++) {
            //get from and to names and refactor the string output to have consistent spacing
            System.out.println(transfers[i].getTransfer_id() + " ACC from..... " + transfers[i].getAccount_from() +
                    " Account to..... " + transfers[i].getAccount_to() + " Amount ..." + transfers[i].getAmount());
        }
        System.out.println("-------------");
        //get user input and if user input == 0, quit, else edit data
        int transferId = consoleService.promptForPendingRequestsIdRequest();
        boolean contains = false;
        if (transferId != 0) {
            for (int i = 0; i < transfers.length; i++) {
                if (transfers[i].getTransfer_id() == transferId) {
                    contains = true;
                    //save transfer here so we can send to server later
                    int transferStatusId = consoleService.promptForPendingTransferMenuARD();
                    if (transferStatusId >= 0 && transferStatusId <= 2) {
                        transfers[i].setTransfer_status_id(transferStatusId + 1);
                        Transfer transfer = transfers[i];
                        transferService.pendingTransferStatusChange(currentUser, transfer);
                    } else {
                        System.out.println("Invalid Selection");
                    }
                }
            }
        }

    }

    private void sendBucks() {
        // TODO Auto-generated method stub
        //not complete ?????????????
        User[] users = null;
        User name_to = null;

        System.out.println("_______________________");
        System.out.println("USERS");
        System.out.println("ID" + "                " + "Name");
        System.out.println("________________________");
        users = userService.getAllUsers(currentUser);
        for (User user : users) {
            int account_id = accountService.getAccountByUserId(currentUser, user.getId());
            System.out.println(account_id + "              " + user.getUsername());
        }


        System.out.println("________________________");


        //create new transfer
        Transfer transfer = new Transfer();
        Account account = null;

//       account= accountService.getAccountByUserId(currentUser, currentUser.getUser().getId());
        int account_from = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId());
        int account_to_choice = consoleService.promptForInt("Enter the receiver's account no: ");
        int account_to = accountService.getAccountByAccountID(currentUser, account_to_choice);


        double amount = consoleService.promptForDouble("Enter the amount: ");

        if (account_to == account_from) {
            System.out.println("Please enter the right account number!");
        } else {

            transfer.setTransfer_type_id(2);
            transfer.setTransfer_status_id(1);
            transfer.setAccount_from(account_from);
            transfer.setAccount_to(account_to);
            transfer.setAmount(amount);

            int transfer_id = accountService.createTransfer(currentUser, transfer);
            System.out.println(currentUser.getUser().getUsername().toUpperCase() + " has sent $" + transfer.getAmount() + " to account #" + transfer.getAccount_to());
        }

        //balance adjustments

        accountService.balanceAdjustmentAfterTransfer(currentUser,transfer);
        System.out.println("_________________________________________");
        System.out.println("Transfers");
        System.out.println("ID"+"             "+"From/To"+"            "+"Amount");
        System.out.println("_________________________________________");
        System.out.println(currentUser.getUser().getId()+"         From: "+currentUser.getUser().getUsername().toUpperCase()+
                "        "+accountService.getBalance(currentUser).getBalance());
        name_to=userService.getUserByAccountID(currentUser,account_to);
        System.out.println(name_to.getId()+"           To: "+name_to.getUsername().toUpperCase()+"         "
                +accountService.getAccToByAccountID(currentUser,account_to).getBalance());

        //transaction details
        System.out.println("\n\n");
        String choice= consoleService.promptForString("Do you want to check the Transaction Detail (y) or (n)");

        if(choice.equalsIgnoreCase("y")) {
            System.out.println("_________________________________________");
            System.out.println("Transfer Details");
            System.out.println("_________________________________________");
            System.out.println("id: " + currentUser.getUser().getId());
            System.out.println("From: " + currentUser.getUser().getUsername().toUpperCase());
            System.out.println("To: " + name_to.getUsername().toUpperCase());
            System.out.println("Type: Send");
            System.out.println("Status: Approved");
            System.out.println("Amount: " + accountService.getBalance(currentUser).getBalance());
        }

    }


	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

}
