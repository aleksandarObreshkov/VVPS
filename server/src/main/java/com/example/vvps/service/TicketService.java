package com.example.vvps.service;

import com.example.vvps.domain.Ticket;
import com.example.vvps.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void deleteTickets(List<Ticket> tickets) {
        for (Ticket t : tickets) {
            t.getTrain().removeTicket(t);
        }
        ticketRepository.deleteAll(tickets);
    }
}
