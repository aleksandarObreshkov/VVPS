package com.example.vvps.domain;

public enum Station {
    SOFIA,
    PLOVDIV,
    BURGAS,
    VARNA,
    ASENOVGRAD,
    SHUMEN,
    PLEVEN,
    DOBRICH;

    public static Station parse(String value) {
        for (Station s : Station.values()) {
            if (s.toString().equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException(String.format("String %s could not be parsed to Station.",value));
    }
}
