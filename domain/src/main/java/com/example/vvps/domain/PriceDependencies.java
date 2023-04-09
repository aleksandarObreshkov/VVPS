package com.example.vvps.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceDependencies {

    public static class PriceDependenciesDiscounts {

        private PriceDependenciesDiscounts() {}
        public static final int ELDERLY = 34;
        public static final int FAMILY_CARD = 50;
        public static final int NOT_RUSH_HOUR = 5;
        public static final int WITH_CHILD = 10;
    }

    private boolean withReturn;
    private boolean elderly;
    private boolean notRushHour;
    private boolean isWithChild;
    private boolean withFamilyCard;

}
