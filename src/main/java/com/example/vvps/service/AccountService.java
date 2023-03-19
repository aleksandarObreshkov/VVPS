package com.example.vvps.service;

import com.example.vvps.domain.Account;
import com.example.vvps.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(String name, boolean isAdmin) {
        Account newAccount = new Account(name, isAdmin);
        accountRepository.save(newAccount);
        return newAccount;
    }

    public void deleteAccount(String accountName) {
        Account account = getAccount(accountName);
        if (account == null) {
            throw new IllegalArgumentException(String.format("No account with name %s found", accountName));
        }
        accountRepository.delete(account);
    }

    public Account getAccount(String name) {
        return accountRepository.findByName(name);
    }
}
