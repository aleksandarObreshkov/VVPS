package com.example.vvps;

import com.example.vvps.domain.Account;
import com.example.vvps.domain.AccountCreationParameters;
import com.example.vvps.domain.AccountCredentials;

import java.util.Scanner;


public class LoginService extends ApiService{

    private final Scanner scanner = new Scanner(System.in);

    public Account authenticateUser() {
        System.out.println("1. Log in");
        System.out.println("2. Register");

        int choice = Integer.parseInt(scanner.nextLine());
        if (choice!=1 && choice!=2) {
            throw new IllegalArgumentException("Invalid input");
        }

        if (choice==1) {
            return logUserIn();
        }
        return registerUser();
    }

    public Account logUserIn() {
        System.out.flush();
        System.out.println("Username: ");
        String username = scanner.nextLine();

        System.out.flush();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        AccountCredentials accountCredentials = new AccountCredentials(username, password);

        return restTemplate.postForEntity(SERVER_URL+"/accounts/login", accountCredentials, Account.class).getBody();
    }

    public Account registerUser() {
        System.out.println("Username: ");
        String username = scanner.nextLine();

        System.out.println("Password: ");
        String password = scanner.nextLine();

        AccountCreationParameters creationParameters = new AccountCreationParameters(username, password, true);
        return restTemplate.postForEntity(SERVER_URL+"/accounts/register", creationParameters, Account.class).getBody();
    }
}
