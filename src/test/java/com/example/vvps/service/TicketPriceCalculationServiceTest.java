package com.example.vvps.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.example.vvps.domain.Course;
import com.example.vvps.domain.PriceDependencies;
import com.example.vvps.domain.Station;
import com.example.vvps.repository.CourseRepository;

class TicketPriceCalculationServiceTest {

    @Mock
    private CourseRepository courseRepository;

    private TicketPriceCalculationService ticketPriceCalculationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        List<Course> stationMap = Arrays.asList(
                new Course(Station.BURGAS, Station.PLOVDIV, 20),
                new Course(Station.DOBRICH, Station.ASENOVGRAD, 30),
                new Course(Station.PLEVEN, Station.SOFIA, 10)
        );
        Mockito.when(courseRepository.findAll()).thenReturn(stationMap);
        ticketPriceCalculationService = new TicketPriceCalculationService(stationMap, courseRepository);
    }

    @Test
    void testCalculatePriceWithNotInRushHourDiscount() {
        Station departureStation = Station.BURGAS;
        Station arrivalStation = Station.PLOVDIV;
        LocalDateTime departureTime = LocalDateTime.now();
        PriceDependencies priceDependencies = new PriceDependencies(false, false, false, false, false, false);
        double expectedPrice = 19;
        assertEquals(expectedPrice, ticketPriceCalculationService.calculatePrice(departureStation, arrivalStation, departureTime, priceDependencies));
    }

    @Test
    void testCalculatePriceInvalidInput() {
        Station departureStation = Station.PLEVEN;
        Station arrivalStation = Station.PLOVDIV;
        LocalDateTime departureTime = LocalDateTime.now();
        PriceDependencies priceDependencies = PriceDependencies.builder().build();
        assertThrows(IllegalArgumentException.class, () -> {
            ticketPriceCalculationService.calculatePrice(departureStation, arrivalStation, departureTime, priceDependencies);
        });
    }

    @Test
    void testCalculatePriceWithElderlyDiscount() {
        Station departureStation = Station.BURGAS;
        Station arrivalStation = Station.PLOVDIV;
        LocalDateTime departureTime = LocalDateTime.of(2023, 12, 12, 17, 0);
        PriceDependencies priceDependencies = PriceDependencies.builder().elderly(true).build();
        int expectedPrice = 18;
        assertEquals(expectedPrice, ticketPriceCalculationService.calculatePrice(departureStation, arrivalStation, departureTime, priceDependencies));
    }

    @Test
    void testCalculatePriceWithStudentDiscount() {
        Station departureStation = Station.BURGAS;
        Station arrivalStation = Station.PLOVDIV;
        LocalDateTime departureTime = LocalDateTime.of(2023, 12, 12, 17, 0);
        PriceDependencies priceDependencies = PriceDependencies.builder().student(true).build();
        int expectedPrice = 16;
        assertEquals(expectedPrice, ticketPriceCalculationService.calculatePrice(departureStation, arrivalStation, departureTime, priceDependencies));
    }

    @Test
    void testCalculatePriceWithCompoundDiscount() {
        Station departureStation = Station.BURGAS;
        Station arrivalStation = Station.PLOVDIV;
        LocalDateTime departureTime = LocalDateTime.of(2023, 12, 12, 12, 0);
        PriceDependencies priceDependencies = PriceDependencies.builder().student(true).build();
        int expectedPrice = 15;
        assertEquals(expectedPrice, ticketPriceCalculationService.calculatePrice(departureStation, arrivalStation, departureTime, priceDependencies));
    }

    @Test
    void testCalculatePriceWithReturn() {
        Station departureStation = Station.BURGAS;
        Station arrivalStation = Station.PLOVDIV;
        LocalDateTime departureTime = LocalDateTime.of(2023, 12, 12, 8, 0);
        PriceDependencies priceDependencies = PriceDependencies.builder().withReturn(true).build();
        int expectedPrice = 40;
        assertEquals(expectedPrice, ticketPriceCalculationService.calculatePrice(departureStation, arrivalStation, departureTime, priceDependencies));
    }
}

