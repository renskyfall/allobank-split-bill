package com.github.renskyfall.splitbill.settlement.model.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SettlementResponse {
    BigDecimal totalExpense;

    BigDecimal amountPerPerson;

    Integer serviceChargePct;

    BigDecimal serviceChargeAmount;

    List<SettlementTransactionResponse> settlements;
}
