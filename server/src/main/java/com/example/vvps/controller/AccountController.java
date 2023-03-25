package com.example.vvps.controller;

import com.example.vvps.domain.Account;
import com.example.vvps.domain.AccountCreationParameters;
import com.example.vvps.repository.AccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping("")
    public ResponseEntity<Account> createAccount(@RequestBody AccountCreationParameters accountCreationParameters) {
        Account account = Account.builder()
                .name(accountCreationParameters.getName())
                .isAdmin(accountCreationParameters.isAdmin())
                .build();
        return ResponseEntity.ok(accountRepository.save(account));
    }

    @GetMapping("")
    public ResponseEntity<List<Account>> getAll() {
        return ResponseEntity.ok(accountRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable String id) {
        return ResponseEntity.of(accountRepository.findById(UUID.fromString(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        accountRepository.deleteById(UUID.fromString(id));
        return ResponseEntity.status(202).build();
    }
}
