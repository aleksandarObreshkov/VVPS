package com.example.vvps.service;

import com.example.vvps.TestData;
import com.example.vvps.domain.PriceDependencies;
import com.example.vvps.domain.Station;
import com.example.vvps.domain.Ticket;
import com.example.vvps.domain.Train;
import com.example.vvps.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TicketServiceTest {

    @Mock
    TicketPriceCalculationService ticketPriceCalculationService;

    @Mock
    TicketRepository ticketRepository;

    @InjectMocks
    TicketService ticketService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteTickets() {
        Train train = TestData.createDummyTrain();
        List<Ticket> tickets = TestData.createDummyTickets(train);
        ticketService.deleteTickets(tickets);
        assertEquals(Collections.emptyList(), train.getTickets());
    }

}
