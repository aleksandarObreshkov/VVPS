package com.example.vvps.controller;

import com.example.vvps.domain.Ticket;
import com.example.vvps.domain.TravelParameters;
import com.example.vvps.repository.TicketRepository;
import com.example.vvps.service.TicketReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketReservationService ticketReservationService;
    private final TicketRepository ticketRepository;

    public TicketController(TicketReservationService ticketReservationService, TicketRepository ticketRepository) {
        this.ticketReservationService = ticketReservationService;
        this.ticketRepository = ticketRepository;
    }

    @PostMapping("/reserve")
    public ResponseEntity<Ticket> reserveTicket(@RequestBody TravelParameters travelParameters) {
        return ResponseEntity.ok(ticketReservationService.reserveTicket(travelParameters));
    }

    @GetMapping("")
    public ResponseEntity<List<Ticket>> getAll() {
        return ResponseEntity.ok(ticketRepository.findAll());
    }

}
