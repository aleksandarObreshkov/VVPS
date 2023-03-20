package com.example.vvps.service;

import com.example.vvps.domain.PriceDependencies;
import com.example.vvps.domain.Station;
import com.example.vvps.domain.Course;
import com.example.vvps.repository.CourseRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
@Component
public class TicketPriceCalculationService {

    private List<Course> stationMap;

    private CourseRepository courseRepository;

    @PostConstruct
    private void fillStationMap() {
        stationMap = courseRepository.findAll();
    }

    public double calculatePrice(Station departureStation, Station arrivalStation, LocalDateTime departureTime,
                                 PriceDependencies priceDependencies) {
        double finalPrice = getInitialTicketPrice(departureStation, arrivalStation);
        priceDependencies.setNotRushHour(isNotInRushHour(departureTime));
        double discountFromCards = calculateDiscountFromCards(priceDependencies);

        return calculateTicketPriceWithDiscounts(finalPrice, discountFromCards, priceDependencies.isWithReturn());
    }

    private double getInitialTicketPrice(Station startStation, Station endStation) {
        for (Course entry : stationMap) {
            if (entry.getDepartureStation().equals(startStation) && entry.getArrivalStation().equals(endStation)) {
                return entry.getPrice();
            }
        }
        throw new IllegalArgumentException(String.format("Could not find a train starting from %s and arriving at %s",
                startStation.toString(), endStation.toString()));
    }

    private boolean isNotInRushHour(LocalDateTime departureTime) {
        return !isInMorningRushHour(departureTime) && !isInEveningRushHour(departureTime);
    }

    private boolean isInMorningRushHour(LocalDateTime departureTime) {
        LocalDateTime start = departureTime.with(LocalTime.of(7, 30));
        LocalDateTime end = departureTime.with(LocalTime.of(9, 30));
        return departureTime.isAfter(start) && departureTime.isBefore(end);
    }

    private boolean isInEveningRushHour(LocalDateTime departureTime) {
        LocalDateTime start = departureTime.with(LocalTime.of(16, 0));
        LocalDateTime end = departureTime.with(LocalTime.of(19, 30));
        return departureTime.isAfter(start) && departureTime.isBefore(end);
    }

    private double calculateDiscountFromCards(PriceDependencies priceDependencies) {
        if (priceDependencies.isWithChild()) {
            if (priceDependencies.isWithFamilyCard()) {
                return PriceDependencies.PriceDependenciesDiscounts.FAMILY_CARD;
            }
            return PriceDependencies.PriceDependenciesDiscounts.WITH_CHILD;
        }

        if (priceDependencies.isElderly()) {
            return PriceDependencies.PriceDependenciesDiscounts.ELDERLY;
        }

        if (priceDependencies.isNotRushHour()) {
            return PriceDependencies.PriceDependenciesDiscounts.NOT_RUSH_HOUR;
        }
        return 0;
    }

    private double calculateTicketPriceWithDiscounts(double initialTicketPrice, double discount, boolean isWithReturn) {
        initialTicketPrice = (100 - discount) * initialTicketPrice / 100.0;

        if (isWithReturn) {
            return initialTicketPrice*2;
        }
        return initialTicketPrice;
    }
}
