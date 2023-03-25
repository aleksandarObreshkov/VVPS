package com.example.vvps.controller;

import com.example.vvps.domain.Account;
import com.example.vvps.domain.AccountCreationParameters;
import com.example.vvps.domain.AccountCredentials;
import com.example.vvps.repository.AccountRepository;
import com.example.vvps.service.AccountService;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<Account> createAccount(@RequestBody AccountCreationParameters accountCreationParameters,
                                                 @RequestHeader("account_id") String id) {
        Account account = Account.builder()
                .name(accountCreationParameters.getName())
                .password(accountCreationParameters.getPassword())
                .isAdmin(accountCreationParameters.isAdmin())
                .build();
        return ResponseEntity.ok(accountRepository.save(account));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAll(@RequestHeader("account_id") String id) {
        if (!accountRepository.getIsAdminById(UUID.fromString(id))) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(accountRepository.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable String id,
                                           @RequestHeader("account_id") String loggedAccountId) {
        if (!accountRepository.getIsAdminById(UUID.fromString(loggedAccountId))) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.of(accountRepository.findById(UUID.fromString(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id,
                                           @RequestHeader("account_id") String loggedAccountId) {
        if (!accountRepository.getIsAdminById(UUID.fromString(loggedAccountId))) {
            return ResponseEntity.status(403).build();
        }
        accountService.deleteById(id);
        return ResponseEntity.status(202).build();
    }
}
