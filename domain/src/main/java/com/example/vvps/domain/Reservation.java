package com.example.vvps.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reservationId;

    @ManyToOne
    @JsonBackReference
    private Account account;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();

    public void addTicket(Ticket t) {
        tickets.add(t);
        t.setReservation(this);
    }

    public void removeTicket(Ticket t) {
        tickets.remove(t);
        t.setReservation(null);
    }
}
