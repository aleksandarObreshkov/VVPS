package com.example.vvps;

import com.example.vvps.domain.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReservationService extends ApiService{

    private final UUID accountId;

    public ReservationService(UUID accountId) {
        this.accountId = accountId;
    }

    public List<Reservation> getReservations() {
        Reservation[] reservations = restTemplate.getForObject(SERVER_URL+"/reservations", Reservation[].class);
        if (reservations!=null) {
            return List.of(reservations);
        }
        return Collections.emptyList();
    }

    public Reservation makeReservation(Train train, ReservationCreationParameters reservationCreationParameters) {
        return restTemplate.postForEntity(SERVER_URL+"/reservations", reservationCreationParameters, Reservation.class).getBody();
    }

    public ReservationCreationParameters getReservationCreationParametersForOneTicket(Scanner scanner, Train train) {
        List<TravelParameters> travelParameters = new ArrayList<>();
        while(true) {
            System.out.println("Name: ");
            String name = scanner.nextLine();
            System.out.println("Departure station: ");
            Station departureStation = Station.parse(scanner.nextLine());
            System.out.println("Arrival station: ");
            Station arrivalStation = Station.parse(scanner.nextLine());

            travelParameters.add(TravelParameters.builder()
                    .passengerName(name)
                    .arrivalStation(arrivalStation)
                    .departureStation(departureStation)
                    .departureTime(train.getDepartureTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm")))
                    .train(train)
                    .build());
            System.out.println("Want to add another ticket? [y,n]");
            String answer = scanner.nextLine();
            if (answer.equals("n")) {
                break;
            }
        }

        System.out.println("Card: ");
        DiscountCard card = DiscountCard.parse(scanner.nextLine());

        return ReservationCreationParameters.builder()
                .ticketsParameters(travelParameters)
                .discountCard(card)
                .build();

    }
}
