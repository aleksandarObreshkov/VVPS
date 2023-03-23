package com.example.vvps.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "train")
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private List<Station> stationRoute;
    private int passengerLimit;
    private LocalDateTime departureTime;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ticket_id")
    @JsonManagedReference
    private List<Ticket> tickets;

    public void addTicket(Ticket t) {
        tickets.add(t);
        t.setTrain(this);
    }

    public void removeTicket(Ticket t) {
        tickets.remove(t);
        t.setTrain(null);
    }
}
