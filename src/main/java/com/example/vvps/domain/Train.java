package com.example.vvps.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Station departureStation;
    private List<Station> stationRoute;
    private int passengerLimit;

    private LocalDateTime departureTime;
}
