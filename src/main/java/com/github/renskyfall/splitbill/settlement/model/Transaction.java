package com.github.renskyfall.splitbill.settlement.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Transaction {

    private String from;

    private String to;

    private BigDecimal amount;
}
