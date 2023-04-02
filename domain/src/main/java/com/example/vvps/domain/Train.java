package com.example.vvps.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "train")
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID trainId;
    private List<Station> stationRoute;
    private int passengerLimit;
    private LocalDateTime departureTime;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
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
