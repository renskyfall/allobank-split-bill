package com.github.renskyfall.splitbill.settlement.service;

import com.github.renskyfall.splitbill.settlement.model.dto.request.SettlementRequest;
import com.github.renskyfall.splitbill.settlement.model.dto.response.SettlementResponse;

public interface SettlementService {

    SettlementResponse calculateSettlement(SettlementRequest request);
    
}
