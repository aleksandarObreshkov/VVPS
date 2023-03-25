package com.example.vvps.service;

import com.example.vvps.domain.PriceDependencies;
import com.example.vvps.domain.Station;
import com.example.vvps.domain.Course;
import com.example.vvps.domain.Train;
import com.example.vvps.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TicketPriceCalculationService {

    public TicketPriceCalculationService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    private final CourseRepository courseRepository;

    public double calculatePrice(Station departureStation, Station arrivalStation, LocalDateTime departureTime,
                                 PriceDependencies priceDependencies, Train train) {
        double finalPrice = getInitialTicketPrice(departureStation, arrivalStation, train);
        priceDependencies.setNotRushHour(isNotInRushHour(departureTime));
        double discountFromCards = calculateDiscountFromCards(priceDependencies);

        return calculateTicketPriceWithDiscounts(finalPrice, discountFromCards, priceDependencies.isWithReturn());
    }

    private double getInitialTicketPrice(Station startStation, Station endStation, Train train) {
        List<Station> stationRoute = train.getStationRoute();
        double initialPrice = 0.0;
        int startIndex = stationRoute.indexOf(startStation);
        int endIndex = stationRoute.indexOf(endStation);

        for (int i = startIndex; i <= endIndex; i+=2) {
            Station departure = stationRoute.get(i);
            Station arrival = stationRoute.get(i+1);
            initialPrice += getPricePerCourse(departure, arrival);
        }
        return initialPrice;

    }

    private double getPricePerCourse(Station start, Station end) {
        List<Course> stationMap = courseRepository.findAll();
        for (Course currentCourse : stationMap) {
            if (currentCourse.getDepartureStation().equals(start) &&
            currentCourse.getArrivalStation().equals(end)) {
                return currentCourse.getPrice();
            }
        }
        throw new IllegalArgumentException(String.format("No such course %s -> %s", start, end));
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
