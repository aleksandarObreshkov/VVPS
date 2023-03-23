package com.example.vvps.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Train train;

    public Ticket(String passengerName, LocalDateTime departureTime, Station departureStation, Station arrivalStation, double price) {
        this.passengerName = passengerName;
        this.departureTime = departureTime;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.price = price;
    }
}
