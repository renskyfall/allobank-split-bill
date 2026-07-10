package com.github.renskyfall.splitbill.settlement.model.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Participant {

    private final String name;

    private final BigDecimal paid;

    private BigDecimal balance;
}
