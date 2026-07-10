package com.github.renskyfall.splitbill.settlement.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import com.github.renskyfall.splitbill.settlement.exception.BadRequestException;

public final class SettlementUtil {

    private SettlementUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static int calculateServiceChargePercentage(String githubUsername) {
        Objects.requireNonNull(githubUsername, "GitHub username must not be null");

        String username = githubUsername.trim().toLowerCase();

        if (username.isEmpty()) {
            throw new BadRequestException("GitHub username must not be empty");
        }

        return username
                .chars()
                .sum() % 10;
    }

    public static BigDecimal calculateServiceChargeAmount(
            BigDecimal totalExpense,
            String githubUsername) {

        Objects.requireNonNull(totalExpense, "Total expense must not be null");

        int percentage = calculateServiceChargePercentage(githubUsername);

        return totalExpense
                .multiply(BigDecimal.valueOf(percentage))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
