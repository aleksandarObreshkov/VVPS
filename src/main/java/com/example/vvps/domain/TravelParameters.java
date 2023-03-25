package com.example.vvps.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TravelParameters {
    private String passengerName;
    private Station departureStation;
    private Station arrivalStation;
    private String departureTime;
    private PriceDependencies priceDependencies;
    private Train train;
}
