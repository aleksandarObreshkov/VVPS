package com.example.vvps.service;

import com.example.vvps.domain.*;
import com.example.vvps.error.NotFoundException;
import com.example.vvps.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(AccountCreationParameters accountCreationParameters, String loggedAccountId) {
        Account account = Account.builder()
                .id(UUID.randomUUID())
                .name(accountCreationParameters.getName())
                .password(accountCreationParameters.getPassword())
                .isAdmin(false)
                .build();
        if (!loggedAccountId.isEmpty() && getIsAdminById(loggedAccountId)) {
            account.setAdmin(accountCreationParameters.isAdmin());
        }
        return accountRepository.save(account);
    }

    public void deleteById(String id) {
        Account account = getById(id);
        List<Reservation> reservations = account.getReservations();
        deleteReservationsFromAccount(account, reservations);
        accountRepository.delete(account);
    }

    public boolean getIsAdminById(String id) {
        Account account = getById(id);
        return account.isAdmin();
    }

    public Account getById(String id) {
        Optional<Account> accountOptional = accountRepository.findById(UUID.fromString(id));
        if (accountOptional.isEmpty()) {
            throw new NotFoundException(String.format("Account with ID %s does not exist", id));
        }
        return accountOptional.get();
    }

    public Account getByUsernameAndPassword(String username, String password) {
        Account account = accountRepository.findByNameAndPassword(username, password);
        if (account == null) {
            throw new IllegalArgumentException(
                    String.format("Account with username %s and password %s does not exist", username, password));
        }
        return account;
    }

    public void updateAccountReservation(Account account, Reservation reservation) {
        account.getReservations().removeIf(r -> r.getReservationId().equals(reservation.getReservationId()));
        account.getReservations().add(reservation);
    }

    private void deleteReservationsFromAccount(Account account, List<Reservation> reservations) {
        List<Reservation> reservationList = List.copyOf(reservations);
        for (Reservation r : reservationList) {
            List<Ticket> tickets = List.copyOf(r.getTickets());
            for (Ticket t : tickets) {
                Train train = t.getTrain();
                train.removeTicket(t);
                r.removeTicket(t);
            }
            account.deleteReservation(r);
        }
    }

    public Account updateAccount(String accountId, AccountCreationParameters accountCreationParameters,
                                 boolean loggedAccountIsAdmin) {
        Account account = getById(accountId);
        account.setName(accountCreationParameters.getName());
        account.setPassword(accountCreationParameters.getPassword());
        if (loggedAccountIsAdmin) {
            account.setAdmin(accountCreationParameters.isAdmin());
        }
        account.setAdmin(false);
        return accountRepository.save(account);
    }

    public void validateUserIsAdmin(String id) throws IllegalAccessException {
        if (!accountRepository.getIsAdminById(UUID.fromString(id))) {
            throw new IllegalAccessException("You don't have access!");
        }
    }


}
