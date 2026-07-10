package com.github.renskyfall.splitbill.settlement.model.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ParticipantRequest {

    @NotBlank(message = "Participant name is required")
    private String name;

    @NotNull(message = "Paid amount is required")
    @PositiveOrZero(message = "Paid amount cannot be negative")
    private BigDecimal paid;
}
