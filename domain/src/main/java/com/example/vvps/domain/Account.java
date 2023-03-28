package com.example.vvps.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    private UUID id;
    private String name;
    private String password;
    private boolean isAdmin;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    public Account(String name, boolean isAdmin) {
        this.name = name;
        this.isAdmin = isAdmin;
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setAccount(this);
    }

    public void deleteReservation(Reservation reservation) {
        this.reservations.remove(reservation);
        reservation.setAccount(null);
    }
}
