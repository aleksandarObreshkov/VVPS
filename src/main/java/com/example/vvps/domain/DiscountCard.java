package com.example.vvps.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DiscountCard {

    ELDERLY,
    FAMILY;
    @JsonCreator
    public static DiscountCard parse(String value) {
        for (DiscountCard c : DiscountCard.values()) {
            if (c.toString().equalsIgnoreCase(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException(String.format("String %s could not be parsed to DiscountCard.",value));
    }
}
