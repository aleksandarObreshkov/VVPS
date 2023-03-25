package com.example.vvps.controller;

import com.example.vvps.domain.Reservation;
import com.example.vvps.domain.ReservationCreationParameters;
import com.example.vvps.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("")
    public ResponseEntity<Reservation> createReservation(@RequestBody
                                                         ReservationCreationParameters reservationCreationParameters) {
        return ResponseEntity.ok(reservationService.createReservation(reservationCreationParameters, false));
    }

    @GetMapping("")
    public ResponseEntity<List<Reservation>> getAll() {
        return ResponseEntity.ok(reservationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getById(@PathVariable String id) {
        return ResponseEntity.ok(reservationService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        reservationService.deleteById(id);
        return ResponseEntity.status(202).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateById(@PathVariable String id,
                                                  @RequestBody
                                                  ReservationCreationParameters reservationCreationParameters) {
        return ResponseEntity.ok(reservationService.updateReservation(id, reservationCreationParameters));
    }
}
