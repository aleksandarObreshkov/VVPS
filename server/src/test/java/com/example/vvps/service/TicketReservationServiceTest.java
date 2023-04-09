package com.example.vvps.service;

import com.example.vvps.TestData;
import com.example.vvps.domain.Station;
import com.example.vvps.domain.Train;
import com.example.vvps.error.NotFoundException;
import com.example.vvps.repository.TrainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TicketReservationServiceTest {

    @Mock
    private TrainRepository trainRepository;
    @InjectMocks
    private TicketReservationService ticketReservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckIfTrainExistsTrue() {
        Train defaultTrain = TestData.createDummyTrain();
        when(trainRepository.findAllByDepartureTime(any(LocalDateTime.class))).thenReturn(List.of(defaultTrain));
        Train train =
                ticketReservationService.checkIfTrainExists(Station.PLOVDIV, Station.ASENOVGRAD, LocalDateTime.now());
        assertEquals(defaultTrain, train);
    }

    @Test
    void testCheckIfTrainExistsFalse() {
        Train defaultTrain = TestData.createDummyTrain();
        LocalDateTime departureTime = LocalDateTime.now();
        Station departureStation = Station.VARNA;
        Station arrivalStation = Station.DOBRICH;
        when(trainRepository.findAllByDepartureTime(departureTime)).thenReturn(List.of(defaultTrain));
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () ->
                ticketReservationService.checkIfTrainExists(departureStation, arrivalStation, departureTime));

        assertEquals(String.format("There is no train leaving from station %s, arriving on station %s, departing at %s",
                departureStation, arrivalStation, departureTime), notFoundException.getError());

    }

}
