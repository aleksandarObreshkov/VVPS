package com.example.vvps.service;

import com.example.vvps.domain.Station;
import com.example.vvps.domain.Ticket;
import com.example.vvps.domain.Train;
import com.example.vvps.domain.TravelParameters;
import com.example.vvps.error.NotFoundException;
import com.example.vvps.repository.TicketRepository;
import com.example.vvps.repository.TrainRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class TicketReservationService {

    private final TicketPriceCalculationService ticketPriceCalculationService;
    private final TicketRepository ticketRepository;
    private final TrainRepository trainRepository;

    private static final String DATE_FORMAT = "dd-MM-yyyy,HH:mm";

    public TicketReservationService(TicketPriceCalculationService ticketPriceCalculationService, TicketRepository ticketRepository, TrainRepository trainRepository) {
        this.ticketPriceCalculationService = ticketPriceCalculationService;
        this.ticketRepository = ticketRepository;
        this.trainRepository = trainRepository;
    }


    @Transactional
    public Ticket reserveTicket(TravelParameters travelParameters) {
        LocalDateTime departureTime;
        try {

            departureTime = LocalDateTime.parse(travelParameters.getDepartureTime(),
                    DateTimeFormatter.ofPattern(DATE_FORMAT));
        } catch (DateTimeParseException dateTimeParseException) {
            throw new IllegalArgumentException("Invalid date format. Should be " + DATE_FORMAT);
        }

        Train train = checkIfTrainExists(travelParameters.getDepartureStation(),
                travelParameters.getArrivalStation(),
                departureTime);

        if (train.getTickets().size() >= train.getPassengerLimit()) {
            throw new IllegalStateException("There are not enough free seats on this train");
        }

        double ticketPrice = ticketPriceCalculationService.calculatePrice(
                travelParameters.getDepartureStation(),
                travelParameters.getArrivalStation(),
                departureTime,
                travelParameters.getPriceDependencies(),
                train);

        Ticket ticket = Ticket.builder()
                .departureStation(travelParameters.getDepartureStation())
                .arrivalStation(travelParameters.getArrivalStation())
                .departureTime(departureTime)
                .passengerName(travelParameters.getPassengerName())
                .price(ticketPrice)
                .train(train)
                .build();

        train.addTicket(ticket);
        return ticketRepository.save(ticket);
    }

    Train checkIfTrainExists(Station departureStation, Station arrivalStation, LocalDateTime departureTime) {
        List<Train> trains = trainRepository.findAllByDepartureTime(departureTime);
        for (Train t : trains) {
            if ((t.getStationRoute().contains(departureStation) && t.getStationRoute().contains(arrivalStation)) &&
                    (t.getStationRoute().indexOf(departureStation) < t.getStationRoute().indexOf(arrivalStation))){
                return t;
            }
        }
        throw new NotFoundException(
                String.format("There is no train leaving from station %s, arriving on station %s, departing at %s",
                        departureStation.toString(),
                        arrivalStation.toString(),
                        departureTime.toString()));
    }
}
