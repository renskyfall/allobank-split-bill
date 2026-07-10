package com.github.renskyfall.splitbill.settlement.model.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SettlementTransactionResponse {

    String from;

    String to;

    BigDecimal amount;
}
