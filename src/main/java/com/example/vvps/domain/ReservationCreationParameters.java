package com.example.vvps.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationCreationParameters {

    private List<TravelParameters> ticketsParameters;
    private DiscountCard discountCard;
    private String accountId;
}
