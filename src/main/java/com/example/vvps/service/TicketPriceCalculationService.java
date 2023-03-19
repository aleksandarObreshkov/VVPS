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
        return calculateTicketPriceWithDiscounts(finalPrice, priceDependencies);
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
        if ((departureTime.getHour() >= 16 && departureTime.getHour() < 19) || (departureTime.getHour() >= 7 && departureTime.getHour() <= 9)) {
            return false;
        }
        return true;
    }

    private double calculateTicketPriceWithDiscounts(double initialTicketPrice, PriceDependencies priceDependencies) {
        double compoundDiscount = 0;
        if (priceDependencies.isStudent()) {
            compoundDiscount+=PriceDependencies.PriceDependenciesDiscounts.STUDENT;
        }

        if (priceDependencies.isElderly()) {
            compoundDiscount+=PriceDependencies.PriceDependenciesDiscounts.ELDERLY;
        }

        if (priceDependencies.isNotRushHour()) {
            compoundDiscount+=PriceDependencies.PriceDependenciesDiscounts.NOT_RUSH_HOUR;
        }

        initialTicketPrice = (100 - compoundDiscount) * initialTicketPrice / 100.0;

        if (priceDependencies.isWithReturn()) {
            return initialTicketPrice*2;
        }
        return initialTicketPrice;
    }

    public double calculatePriceOld(Station start, Station end, boolean withReturn) {
        double finalPrice = getInitialTicketPrice(start, end);

        if (withReturn) {
            return finalPrice*2;
        }

        return finalPrice;
    }
}
