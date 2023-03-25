package com.example.vvps.repository;

import com.example.vvps.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findByName(String name);
    Account findByNameAndPassword(String name, String password);
}
