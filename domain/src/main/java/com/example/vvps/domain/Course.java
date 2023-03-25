package com.example.vvps.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Station departureStation;
    private Station arrivalStation;
    private double price;

    public Course(Station departureStation, Station arrivalStation, double price) {
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.price = price;
    }
}
