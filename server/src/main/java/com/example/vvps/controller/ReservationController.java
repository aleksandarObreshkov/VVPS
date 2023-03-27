package com.example.vvps.controller;

import com.example.vvps.domain.Reservation;
import com.example.vvps.domain.ReservationCreationParameters;
import com.example.vvps.service.AccountService;
import com.example.vvps.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final AccountService accountService;

    public ReservationController(ReservationService reservationService, AccountService accountService) {
        this.reservationService = reservationService;
        this.accountService = accountService;
    }

    @PostMapping("")
    public ResponseEntity<Reservation> createReservation(@RequestBody
                                                         ReservationCreationParameters reservationCreationParameters) {
        return ResponseEntity.ok(reservationService.createReservation(reservationCreationParameters, false));
    }

    @GetMapping("")
    public ResponseEntity<List<Reservation>> getAll(@RequestHeader("account_id") String accountId) {
        if (accountService.getIsAdminById(accountId)) {
            return ResponseEntity.ok(reservationService.getAll());
        }
        else {
            return ResponseEntity.ok(accountService.getById(accountId).getReservations());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getById(@PathVariable String id,
                                               @RequestHeader("account_id") String accountId) throws IllegalAccessException {
        Reservation reservation = reservationService.getById(id);
        if (!reservation.getAccount().getId().equals(UUID.fromString(accountId)) &&
                !reservation.getAccount().isAdmin()) {
            throw new IllegalAccessException("You don't have access to this reservation");
        }
        return ResponseEntity.ok(reservationService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id,
                                           @RequestHeader("account_id") String accountId) throws IllegalAccessException {
        Reservation reservation = reservationService.getById(id);
        if (!reservation.getAccount().getId().equals(UUID.fromString(accountId)) &&
                !reservation.getAccount().isAdmin()) {
            throw new IllegalAccessException("You don't have access to this reservation");
        }
        reservationService.deleteById(id);
        return ResponseEntity.status(202).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateById(@PathVariable String id,
                                                  @RequestBody ReservationCreationParameters reservationCreationParameters,
                                                  @RequestHeader("account_id")String accountId) throws IllegalAccessException {
        Reservation reservation = reservationService.getById(id);
        if (!reservation.getAccount().getId().equals(UUID.fromString(accountId)) &&
                !reservation.getAccount().isAdmin()) {
            throw new IllegalAccessException("You don't have access to this reservation");
        }
        return ResponseEntity.ok(reservationService.updateReservation(id, reservationCreationParameters));
    }
}
