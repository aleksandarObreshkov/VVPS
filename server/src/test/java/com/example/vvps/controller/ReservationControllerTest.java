package com.example.vvps.controller;

import com.example.vvps.TestData;
import com.example.vvps.domain.Account;
import com.example.vvps.domain.Reservation;
import com.example.vvps.domain.ReservationCreationParameters;
import com.example.vvps.service.AccountService;
import com.example.vvps.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationControllerTest {

    @Mock
    ReservationService reservationService;
    @Mock
    AccountService accountService;

    @InjectMocks
    ReservationController reservationController;

    private static final String SEARCHED_ID = UUID.randomUUID().toString();
    private static final String LOGGED_USER_ID = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllWithAdminLoggedUser() {
        when(accountService.getIsAdminById(any())).thenReturn(true);
        reservationController.getAll(LOGGED_USER_ID);
        verify(reservationService).getAll();
    }

    @Test
    void testGetAllWithLoggedNonAdminUser() {
        Account account = TestData.getNonAdminAccount();
        account.setReservations(TestData.createReservations(account));
        when(accountService.getById(any())).thenReturn(account);
        when(accountService.getIsAdminById(any())).thenReturn(false);
        reservationController.getAll(LOGGED_USER_ID);
        verify(accountService).getById(LOGGED_USER_ID);
    }

    @Test
    void testGetByIdWithAdminUser() throws IllegalAccessException {
        Account account = TestData.getAdminAccount();
        List<Reservation> reservations = TestData.createReservations(account);
        account.setReservations(reservations);

        when(reservationService.getById(any())).thenReturn(reservations.get(0));
        when(accountService.getIsAdminById(any())).thenReturn(true);
        reservationController.getById(SEARCHED_ID, LOGGED_USER_ID);
        verify(reservationService, times(2)).getById(SEARCHED_ID);
    }

    @Test
    void testGetByIdWithNonAdminUserAndOthersReservation() {
        Account account = TestData.getAdminAccount();
        List<Reservation> reservations = TestData.createReservations(account);
        account.setReservations(reservations);

        when(accountService.getIsAdminById(any())).thenReturn(false);
        when(reservationService.getById(any())).thenReturn(reservations.get(0));
        Throwable t = assertThrows(IllegalAccessException.class,
                () -> reservationController.getById(SEARCHED_ID, LOGGED_USER_ID));
        assertEquals(ReservationController.ACCESS_DENIED_ERROR_MESSAGE, t.getMessage());
    }

    @Test
    void testGetByIdWithNonAdminUserAndOwnReservation() throws IllegalAccessException {
        Account account = TestData.getAdminAccount();
        List<Reservation> reservations = TestData.createReservations(account);
        account.setReservations(reservations);

        when(accountService.getIsAdminById(any())).thenReturn(false);
        when(reservationService.getById(any())).thenReturn(reservations.get(0));
        ResponseEntity<Reservation> reservation = reservationController.getById(reservations.get(0).getReservationId().toString(),
                        account.getId().toString());
        assertEquals(HttpStatus.OK, reservation.getStatusCode());
    }

    @Test
    void testDeleteByIdWithAdminUser() throws IllegalAccessException {
        Account account = TestData.getAdminAccount();
        List<Reservation> reservations = TestData.createReservations(account);
        account.setReservations(reservations);

        when(reservationService.getById(any())).thenReturn(reservations.get(0));
        when(accountService.getIsAdminById(any())).thenReturn(true);
        reservationController.deleteById(SEARCHED_ID, LOGGED_USER_ID);
        verify(reservationService).deleteById(SEARCHED_ID);
    }

    @Test
    void testDeleteByIdWithNonAdminUserAndOthersReservation() {
        Account account = TestData.getAdminAccount();
        List<Reservation> reservations = TestData.createReservations(account);
        account.setReservations(reservations);

        when(accountService.getIsAdminById(any())).thenReturn(false);
        when(reservationService.getById(any())).thenReturn(reservations.get(0));

        Throwable t = assertThrows(IllegalAccessException.class,
                () -> reservationController.deleteById(SEARCHED_ID, LOGGED_USER_ID));
        assertEquals(ReservationController.ACCESS_DENIED_ERROR_MESSAGE, t.getMessage());
    }

    @Test
    void testDeleteByIdWithNonAdminUserAndOwnReservation() throws IllegalAccessException {
        Account account = TestData.getNonAdminAccount();
        List<Reservation> reservations = TestData.createReservations(account);
        account.setReservations(reservations);

        when(accountService.getIsAdminById(any())).thenReturn(false);
        when(reservationService.getById(any())).thenReturn(reservations.get(0));

        ResponseEntity<Void> response = reservationController.deleteById(reservations.get(0).getReservationId().toString(),
                        account.getId().toString());
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    void testUpdateByIdWithAdminUser() throws IllegalAccessException {
        Account account = TestData.getNonAdminAccount();
        List<Reservation> reservations = TestData.createReservations(account);
        account.setReservations(reservations);

        when(accountService.getIsAdminById(any())).thenReturn(true);
        when(reservationService.getById(any())).thenReturn(reservations.get(0));

        ReservationCreationParameters reservationCreationParameters = new ReservationCreationParameters();
        reservationController.updateById(SEARCHED_ID, reservationCreationParameters, LOGGED_USER_ID);
        verify(reservationService).updateReservation(SEARCHED_ID, reservationCreationParameters);
    }

    @Test
    void testUpdateByIdWithNonAdminUserAndOthersReservation() {
        Account account = TestData.getAdminAccount();
        List<Reservation> reservations = TestData.createReservations(account);
        account.setReservations(reservations);

        when(accountService.getIsAdminById(any())).thenReturn(false);
        when(reservationService.getById(any())).thenReturn(reservations.get(0));

        Throwable t = assertThrows(IllegalAccessException.class,
                () -> reservationController.updateById(SEARCHED_ID, new ReservationCreationParameters(),
                        LOGGED_USER_ID));
        assertEquals(ReservationController.ACCESS_DENIED_ERROR_MESSAGE, t.getMessage());
    }

    @Test
    void testUpdateByIdWithNonAdminUserAndOwnReservation() throws IllegalAccessException {
        Account account = TestData.getNonAdminAccount();
        List<Reservation> reservations = TestData.createReservations(account);
        account.setReservations(reservations);

        when(accountService.getIsAdminById(any())).thenReturn(false);
        when(reservationService.getById(any())).thenReturn(reservations.get(0));

        ResponseEntity<Reservation> response = reservationController.updateById(
                reservations.get(0).getReservationId().toString(),
                new ReservationCreationParameters(),
                account.getId().toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
