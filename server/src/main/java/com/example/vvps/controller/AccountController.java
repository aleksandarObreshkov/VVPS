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
    public ResponseEntity<Account> login(@RequestBody AccountCredentials accountCredentials) {
        return ResponseEntity.ok(accountService.getByUsernameAndPassword(accountCredentials.getUsername(),
                accountCredentials.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<Account> createAccount(@RequestBody AccountCreationParameters accountCreationParameters,
                                                 @RequestHeader("account_id") String accountId) {

        return ResponseEntity.ok(accountService.createAccount(accountCreationParameters, accountId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAll(@RequestHeader("account_id") String id) throws IllegalAccessException {
        accountService.validateUserIsAdmin(id);
        return ResponseEntity.ok(accountRepository.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable String id,
                                           @RequestHeader("account_id") String loggedAccountId) throws IllegalAccessException {
        Account searchedAccount = accountService.getById(id);
        if (!searchedAccount.getId().toString().equals(loggedAccountId)) {
            accountService.validateUserIsAdmin(loggedAccountId);
        }
        return ResponseEntity.of(accountRepository.findById(UUID.fromString(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id,
                                           @RequestHeader("account_id") String loggedAccountId) throws IllegalAccessException {
        accountService.validateUserIsAdmin(loggedAccountId);
        accountService.deleteById(id);
        return ResponseEntity.status(202).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateById(@PathVariable String id,
                                              @RequestHeader("account_id") String loggedAccountId,
                                              @RequestBody AccountCreationParameters accountCreationParameters) throws IllegalAccessException {
        Account searchedAccount = accountService.getById(id);
        boolean isLoggedUserAdmin = false;
        if (!searchedAccount.getId().toString().equals(loggedAccountId)) {
            accountService.validateUserIsAdmin(loggedAccountId);
            isLoggedUserAdmin = true;
        }
        return ResponseEntity.ok(accountService.updateAccount(id, accountCreationParameters, isLoggedUserAdmin));
    }
}
