package com.example.vvps.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.vvps.domain.PriceDependencies;
import com.example.vvps.domain.Station;
import com.example.vvps.domain.Train;
import com.example.vvps.domain.TravelParameters;
import com.example.vvps.repository.TicketRepository;
import com.example.vvps.repository.TrainRepository;

public class TicketReservationServiceTest {

    @Mock
    private TicketPriceCalculationService ticketPriceCalculationService;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private TrainRepository trainRepository;

    private TicketReservationService ticketReservationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ticketReservationService = new TicketReservationService(ticketPriceCalculationService, ticketRepository,
                trainRepository);
    }

    @Test
    public void testReserveTicketWithNonExistentTrain() {
        Station departureStation = Station.PLOVDIV;
        Station arrivalStation = Station.SOFIA;
        LocalDateTime departureTime = LocalDateTime.now();

        TravelParameters travelParameters = TravelParameters.builder()
                .departureStation(departureStation)
                .arrivalStation(arrivalStation)
                .departureTime(departureTime)
                .priceDependencies(new PriceDependencies())
                .build();

        when(trainRepository.findAllByDepartureStationAndDepartureTimeAndStationRouteIn(departureStation, arrivalStation,
                departureTime)).thenReturn(Collections.emptyList());

        Throwable t = assertThrows(IllegalArgumentException.class, () -> ticketReservationService.reserveTicket(travelParameters));
        assertEquals(t.getMessage(),
                String.format("There is no train leaving from station %s, arriving on station %s, departing at %s",
                        departureStation,
                        arrivalStation,
                        departureTime));
    }

    @Test
    public void testReserveTicketWithExistingTrain() {
        Station departureStation = Station.ASENOVGRAD;
        Station arrivalStation = Station.SHUMEN;
        LocalDateTime departureTime = LocalDateTime.now();

        TravelParameters travelParameters = TravelParameters.builder()
                .departureStation(departureStation)
                .arrivalStation(arrivalStation)
                .departureTime(departureTime)
                .priceDependencies(new PriceDependencies())
                .build();

        Train train = new Train();
        when(trainRepository.findAllByDepartureStationAndDepartureTimeAndStationRouteIn(departureStation, arrivalStation,
                departureTime)).thenReturn(Collections.singletonList(train));

        assertDoesNotThrow(() -> ticketReservationService.reserveTicket(travelParameters),
                "Should not throw an exception when train is found");
    }

}

