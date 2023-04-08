package com.example.vvps;

import com.example.vvps.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestData {

    private static final String ADMIN_USERNAME = "root";
    private static final String ADMIN_PASSWORD = "root";

    private static final String NON_ADMIN_USERNAME = "non-root";
    private static final String NON_ADMIN_PASSWORD = "non-root";

    public static Account getAdminAccount() {
        return Account.builder()
                .id(UUID.randomUUID())
                .name(ADMIN_USERNAME)
                .password(ADMIN_PASSWORD)
                .isAdmin(true)
                .build();
    }

    public static Account getNonAdminAccount() {
        return Account.builder()
                .id(UUID.randomUUID())
                .name(NON_ADMIN_USERNAME)
                .password(NON_ADMIN_PASSWORD)
                .isAdmin(false)
                .build();
    }

    public static Account getDummyAccount(boolean isAdmin) {
        AccountCreationParameters accountCreationParameters = AccountCreationParameters.builder()
                .name("John Doe")
                .password("password")
                .isAdmin(isAdmin)
                .build();

        return getDummyAccount(accountCreationParameters);
    }

    public static Account getDummyAccount(AccountCreationParameters accountCreationParameters) {
        return Account.builder()
                .id(UUID.randomUUID())
                .name(accountCreationParameters.getName())
                .password(accountCreationParameters.getPassword())
                .isAdmin(accountCreationParameters.isAdmin())
                .build();
    }

    public static List<Reservation> createReservations(Account account) {
        Train train = createDummyTrain();
        List<Ticket> dummyTickets = createDummyTickets(train);
        train.setTickets(dummyTickets);
        Reservation reservation = Reservation.builder()
                .reservationId(UUID.randomUUID())
                .tickets(dummyTickets)
                .account(account)
                .build();
        List<Reservation> result = new ArrayList<>();
        result.add(reservation);
        return result;
    }

    public static Train createDummyTrain() {
        List<Station> trainStationRoute = List.of(Station.DOBRICH, Station.SHUMEN, Station.PLEVEN, Station.SOFIA,
                Station.PLOVDIV, Station.ASENOVGRAD, Station.BURGAS, Station.VARNA);
        return Train.builder()
                .trainId(UUID.randomUUID())
                .departureTime(LocalDateTime.now())
                .stationRoute(trainStationRoute)
                .tickets(new ArrayList<>())
                .build();
    }

    public static List<Ticket> createDummyTickets(Train train) {
        List<Ticket> result = new ArrayList<>();
        Ticket a = Ticket.builder()
                .train(train)
                .price(10)
                .passengerName("dummy")
                .departureStation(Station.PLOVDIV)
                .arrivalStation(Station.ASENOVGRAD)
                .departureTime(train.getDepartureTime())
                .build();

        Ticket b = Ticket.builder()
                .train(train)
                .price(20)
                .passengerName("dummy1")
                .departureStation(Station.PLOVDIV)
                .arrivalStation(Station.BURGAS)
                .departureTime(train.getDepartureTime())
                .build();
        result.add(a);
        result.add(b);
        return result;
    }

    public static List<Ticket> createDummyTickets() {
        return createDummyTickets(createDummyTrain());
    }

}
