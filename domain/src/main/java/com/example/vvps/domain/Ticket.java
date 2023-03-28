package com.example.vvps.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "ticket")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String passengerName;
    private LocalDateTime departureTime;
    private Station departureStation;
    private Station arrivalStation;
    private double price;
    @ManyToOne
    //@JoinColumn(name = "train_id")
    @JsonBackReference(value = "train_reference")
    private Train train;
    @ManyToOne
    //@JoinColumn(name="reservation_id")
    @JsonBackReference(value = "reservation_reference")
    private Reservation reservation;
}
