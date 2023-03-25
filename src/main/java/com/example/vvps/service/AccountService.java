package com.example.vvps.service;

import com.example.vvps.domain.Account;
import com.example.vvps.domain.Reservation;
import com.example.vvps.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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

    public Account getById(String id) {
        Optional<Account> accountOptional = accountRepository.findById(UUID.fromString(id));
        if (accountOptional.isEmpty()) {
            throw new IllegalArgumentException(String.format("Account with ID %s does not exist", id));
        }
        return accountOptional.get();
    }

    public void updateAccountReservation(Account account, Reservation reservation) {
        for (Reservation r : account.getReservations()) {
            if (r.getId().equals(reservation.getId())) {
                account.getReservations().remove(r);
            }
        }
        account.getReservations().add(reservation);
    }
}
