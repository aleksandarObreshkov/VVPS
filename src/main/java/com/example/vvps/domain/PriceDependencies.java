package com.example.vvps.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceDependencies {

    public static class PriceDependenciesDiscounts {
        public static final int ELDERLY = 10;
        public static final int STUDENT = 20;
        public static final int NOT_RUSH_HOUR = 5;
    }

    private boolean withReturn;
    private boolean elderly;
    private boolean notRushHour;
    private boolean student;


}
