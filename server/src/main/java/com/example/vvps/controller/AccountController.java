package com.example.vvps.controller;

import com.example.vvps.domain.Account;
import com.example.vvps.domain.AccountCreationParameters;
import com.example.vvps.domain.AccountCredentials;
import com.example.vvps.repository.AccountRepository;
import com.example.vvps.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public AccountController(AccountRepository accountRepository, AccountService accountService) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @PostMapping("/login")
    public ResponseEntity<Account> getByCredentials(@RequestBody AccountCredentials accountCredentials) {
        return ResponseEntity.ok(accountRepository.findByNameAndPassword(accountCredentials.getUsername(),
                accountCredentials.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<Account> createAccount(@RequestBody AccountCreationParameters accountCreationParameters) {
        Account account = Account.builder()
                .id(UUID.randomUUID())
                .name(accountCreationParameters.getName())
                .password(accountCreationParameters.getPassword())
                .isAdmin(accountCreationParameters.isAdmin())
                .build();
        return ResponseEntity.ok(accountRepository.save(account));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAll(@RequestHeader("account_id") String id) throws IllegalAccessException {
        if (!accountService.getIsAdminById(id)) {
            throw new IllegalAccessException("You don't have access to all accounts");
        }
        return ResponseEntity.ok(accountRepository.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable String id,
                                           @RequestHeader("account_id") String loggedAccountId) throws IllegalAccessException {
        if (!accountRepository.getIsAdminById(UUID.fromString(loggedAccountId))) {
            throw new IllegalAccessException("You don't have access to this account");
        }
        return ResponseEntity.of(accountRepository.findById(UUID.fromString(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id,
                                           @RequestHeader("account_id") String loggedAccountId) throws IllegalAccessException {
        if (!accountRepository.getIsAdminById(UUID.fromString(loggedAccountId))) {
            throw new IllegalAccessException("You don't have access to this account");
        }
        accountService.deleteById(id);
        return ResponseEntity.status(202).build();
    }
}
