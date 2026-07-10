package com.github.renskyfall.splitbill.settlement.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class SettlementUtilTest {

    @Test
    void shouldCalculateServiceChargePercentage() {

        int percentage =
                SettlementUtil.calculateServiceChargePercentage("renskyfall");

        assertEquals(
                3,
                percentage
        );

    }

    @Test
    void shouldCalculateServiceChargeAmount() {

        BigDecimal amount =
                SettlementUtil.calculateServiceChargeAmount(
                        new BigDecimal("100"),
                        "renskyfall"
                );

        assertEquals(
                new BigDecimal("3.00"),
                amount
        );

    }

    @Test
    void shouldReturnZeroPercentForJohnDoe47() {

    int percentage =
            SettlementUtil.calculateServiceChargePercentage("johnDoe47");

    assertEquals(0, percentage);

}
}
