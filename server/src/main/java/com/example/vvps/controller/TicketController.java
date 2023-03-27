package com.example.vvps.controller;

import com.example.vvps.domain.Ticket;
import com.example.vvps.service.AccountService;
import com.example.vvps.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final AccountService accountService;
    private final TicketService ticketService;

    public TicketController(AccountService accountService, TicketService ticketService) {
        this.accountService = accountService;
        this.ticketService = ticketService;
    }

    @GetMapping("")
    public ResponseEntity<List<Ticket>> getAll(@RequestHeader("account_id") String accountId) {
        if (!accountService.getIsAdminById(accountId)) {
            return ResponseEntity.ok(ticketService.getAllByAccountId(accountId));
        }
        return ResponseEntity.ok(ticketService.getAll());
    }

}
