package com.example.vvps;

import com.example.vvps.domain.Account;
import com.example.vvps.domain.Reservation;
import com.example.vvps.domain.ReservationCreationParameters;
import com.example.vvps.domain.Train;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws JsonProcessingException {
        LoginService loginService = new LoginService();
        Scanner scanner = new Scanner(System.in);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Account account = loginService.authenticateUser();
        System.out.printf("Successfully logged in as '%s'%n", account.getName());
        printMenu(account.isAdmin());

        int option = Integer.parseInt(scanner.nextLine());
        if (option < 1 || option > 8) {
            System.out.println("Invalid input");
            System.exit(1);
        }

        switch (option) {
            case 1: {
                ReservationService reservationService = new ReservationService(account.getId());
                List<Reservation> reservations = reservationService.getReservations();
                System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(reservations));
                break;
            }
            case 2: {
                System.out.println("Train ID: ");
                String id = scanner.nextLine();
                TrainService trainService = new TrainService();
                Train train = trainService.getTrainById(id);
                ReservationService reservationService = new ReservationService(account.getId());
                ReservationCreationParameters rParameters =
                        reservationService.getReservationCreationParametersForOneTicket(scanner, train);
                Reservation reservation = reservationService.makeReservation(train, rParameters);
                System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(reservation));
                break;
            }
        }

    }

    private static void printMenu(boolean isAdmin) {
        System.out.println("Menu: ");
        System.out.println("1. View all your reservations");
        System.out.println("2. Make a new reservation");
        System.out.println("3. Search train by departure time");
        System.out.println("4. Search train by departure station");
        if (isAdmin) {
            System.out.println("5. View all accounts");
            System.out.println("6. Edit an account");
            System.out.println("7. Delete an account");
            System.out.println("8. Apply discount");
        }
    }
}
