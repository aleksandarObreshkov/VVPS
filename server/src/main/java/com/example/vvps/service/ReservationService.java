package com.example.vvps.service;

import com.example.vvps.domain.*;
import com.example.vvps.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationService{
    private final ReservationRepository reservationRepository;

    private final TicketReservationService ticketReservationService;
    private final TicketService ticketService;
    private final AccountService accountService;

    public ReservationService(ReservationRepository reservationRepository,
                              TicketReservationService ticketReservationService,
                              TicketService ticketService,
                              AccountService accountService) {
        this.reservationRepository = reservationRepository;
        this.ticketReservationService = ticketReservationService;
        this.ticketService = ticketService;
        this.accountService = accountService;
    }

    @Transactional
    public Reservation createReservation(ReservationCreationParameters reservationCreationParameters, boolean isUpdate) {
        Account account = accountService.getById(reservationCreationParameters.getAccountId());

        List<Ticket> tickets = createTickets(reservationCreationParameters.getTicketsParameters());

        Reservation reservation = Reservation.builder()
                .tickets(tickets)
                .account(account)
                .build();

        Reservation result = reservationRepository.save(reservation);
        if (isUpdate) {
            accountService.updateAccountReservation(account, result);
        }
        return result;
    }

    public Reservation getById(String id) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(UUID.fromString(id));
        if (reservationOptional.isEmpty()) {
            throw new IllegalArgumentException(String.format("Reservation with ID '%s' could not be found", id));
        }
        return reservationOptional.get();
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void deleteById(String id) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(UUID.fromString(id));
        if (reservationOptional.isEmpty()) {
            throw new IllegalArgumentException(String.format("Reservation with ID '%s' does not exist.", id));
        }

        delete(reservationOptional.get());
    }

    @Transactional
    public void delete(Reservation reservation) {
        reservation.getAccount().getReservations().remove(reservation);
        ticketService.deleteTickets(reservation.getTickets());
        reservationRepository.delete(reservation);
    }

    @Transactional
    public Reservation updateReservation(String id, ReservationCreationParameters reservationCreationParameters){
        Reservation reservation = getById(id);
        removeTickets(reservation);

        List<Ticket> newTickets = createTickets(reservationCreationParameters.getTicketsParameters());
        for (Ticket t : newTickets) {
            reservation.addTicket(t);
        }

        return reservationRepository.save(reservation);
    }

    @Transactional
    void removeTickets(Reservation reservation) {
        List<Ticket> tickets = List.copyOf(reservation.getTickets());
        for (Ticket t : tickets) {
            reservation.removeTicket(t);
        }
        ticketService.deleteTickets(tickets);
        reservationRepository.save(reservation);
    }

    private List<Ticket> createTickets(List<TravelParameters> travelParameters) {
        List<Ticket> tickets = new ArrayList<>();
        for (TravelParameters params : travelParameters) {
            tickets.add(ticketReservationService.reserveTicket(params));
        }
        return tickets;
    }

}
