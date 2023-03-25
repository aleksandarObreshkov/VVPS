package com.example.vvps.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountCreationParameters {

    private String name;
    private boolean isAdmin;
}
