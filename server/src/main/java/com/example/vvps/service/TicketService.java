package com.example.vvps.service;

import com.example.vvps.domain.PriceDependencies;
import com.example.vvps.domain.Reservation;
import com.example.vvps.domain.Ticket;
import com.example.vvps.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketPriceCalculationService ticketPriceCalculationService;

    public TicketService(TicketRepository ticketRepository, TicketPriceCalculationService ticketPriceCalculationService) {
        this.ticketRepository = ticketRepository;
        this.ticketPriceCalculationService = ticketPriceCalculationService;
    }

    public void deleteTickets(List<Ticket> tickets) {
        for (Ticket t : tickets) {
            t.getTrain().removeTicket(t);
        }
        ticketRepository.deleteAll(tickets);
    }

    public void setTicketReservations(List<Ticket>tickets, Reservation reservation) {
        for (Ticket t : tickets) {
            t.setReservation(reservation);
        }
        ticketRepository.saveAll(tickets);
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

    public Ticket getById(String ticketId) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(UUID.fromString(ticketId));
        if (ticketOptional.isEmpty()) {
            throw new IllegalArgumentException(String.format("Ticket with ID '%s' does not exist"));
        }
        return ticketOptional.get();
    }

    public void updateTicketDiscounts(Ticket ticket, PriceDependencies priceDependencies) {
        double price = ticketPriceCalculationService.calculatePrice(
                ticket.getDepartureStation(),
                ticket.getArrivalStation(),
                ticket.getDepartureTime(),
                priceDependencies,
                ticket.getTrain());
        ticket.setPrice(price);
        ticketRepository.save(ticket);
    }
}
