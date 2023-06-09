package com.example.vvps.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AccountCreationParameters {

    private String name;
    private String password;
    private boolean isAdmin;
}
