package com.example.vvps.service;

import com.example.vvps.domain.Ticket;
import com.example.vvps.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public List<Ticket> getAll() {
        return ticketRepository.findAll();
    }

    public List<Ticket> getAllByAccountId(String accountId) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t: getAll()) {
            if (t.getReservation().getAccount().getId().equals(UUID.fromString(accountId))) {
                result.add(t);
            }
        }
        return result;
    }
}
