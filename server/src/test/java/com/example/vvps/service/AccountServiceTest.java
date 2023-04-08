package com.example.vvps.service;

import com.example.vvps.TestData;
import com.example.vvps.domain.*;
import com.example.vvps.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    private final Account adminAccount = TestData.getAdminAccount();
    private final Account nonAdminAccount = TestData.getNonAdminAccount();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAdminAccountWithLoggedAdmin() {
        AccountCreationParameters accountCreationParameters = AccountCreationParameters.builder()
                .name("John Doe")
                .password("password")
                .isAdmin(true)
                .build();
        Account account = TestData.getDummyAccount(accountCreationParameters);


        when(accountRepository.save(any())).thenReturn(account);
        when(accountRepository.findById(adminAccount.getId())).thenReturn(Optional.of(adminAccount));

        Account createdAccount =
                accountService.createAccount(accountCreationParameters, adminAccount.getId().toString());

        assertNotNull(createdAccount);
        assertEquals(accountCreationParameters.getName(), createdAccount.getName());
        assertEquals(accountCreationParameters.getPassword(), createdAccount.getPassword());
        assertTrue(createdAccount.isAdmin());
    }

    @Test
    void testCreateAdminAccountWithLoggedNonAdmin() {
        AccountCreationParameters accountCreationParameters = AccountCreationParameters.builder()
                .name("John Doe")
                .password("password")
                .isAdmin(true)
                .build();


        when(accountRepository.findById(nonAdminAccount.getId())).thenReturn(Optional.of(nonAdminAccount));

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        accountService.createAccount(accountCreationParameters, nonAdminAccount.getId().toString());

        verify(accountRepository).save(accountArgumentCaptor.capture());
        Account createdAccount = accountArgumentCaptor.getValue();

        assertNotNull(createdAccount);
        assertEquals(accountCreationParameters.getName(), createdAccount.getName());
        assertEquals(accountCreationParameters.getPassword(), createdAccount.getPassword());
        assertFalse(createdAccount.isAdmin());
    }

    @Test
    void testDeleteById() {
        Account account = TestData.getDummyAccount(false);
        List<Reservation> reservations = TestData.createReservations(account);
        account.setReservations(reservations);

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        accountService.deleteById(account.getId().toString());
        assertEquals(Collections.emptyList(), account.getReservations());
    }

    @Test
    void testUpdateAccountReservations() {
        Account account = TestData.getDummyAccount(false);
        List<Reservation> reservations = TestData.createReservations(account);
        account.setReservations(reservations);

        int newPrice = 50;

        List<Reservation> newReservations = List.copyOf(reservations);
        Reservation reservation = newReservations.get(0);
        Ticket ticketToUpdate = reservation.getTickets().get(0);
        ticketToUpdate.setPrice(newPrice);

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        accountService.updateAccountReservation(account, reservation);
        assertEquals(account.getReservations().get(0).getTickets().get(0).getPrice(), newPrice);
    }

    @Test
    void testCheckIsAdminByIdThrowsException() {
        when(accountRepository.getIsAdminById(UUID.randomUUID()))
                .thenReturn(false);
        Throwable t = assertThrows(IllegalAccessException.class, ()->
                accountService.validateUserIsAdmin(UUID.randomUUID().toString()));
        assertEquals("You don't have access to this account", t.getMessage());
    }

    @Test
    void testCheckIsAdminByIdValidates() throws IllegalAccessException {
        when(accountRepository.getIsAdminById(any()))
                .thenReturn(true);
        accountService.validateUserIsAdmin(UUID.randomUUID().toString());
    }

    @Test
    void testFindByUsernameAndPasswordThrowsException() {
        String username = "hello";
        String password = "hello";
        when(accountRepository.findByNameAndPassword(username, password))
                .thenReturn(null);
        Throwable t = assertThrows(IllegalArgumentException.class, ()->
                accountService.getByUsernameAndPassword(username, password));
        assertEquals(String.format("Account with username %s and password %s does not exist", username, password),
                t.getMessage());
    }

    @Test
    void testFindByUsernameAndPasswordPasses() {
        String username = "hello";
        String password = "hello";
        when(accountRepository.findByNameAndPassword(username, password))
                .thenReturn(Account.builder().name(username).password(password).build());
        Account accountFromDB = accountService.getByUsernameAndPassword(username, password);
        assertEquals(username, accountFromDB.getName());
        assertEquals(password, accountFromDB.getPassword());
    }




}