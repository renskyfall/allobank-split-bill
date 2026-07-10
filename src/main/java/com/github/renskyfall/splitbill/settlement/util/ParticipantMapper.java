package com.github.renskyfall.splitbill.settlement.util;

import java.math.BigDecimal;

import com.github.renskyfall.splitbill.settlement.model.dto.Participant;
import com.github.renskyfall.splitbill.settlement.model.dto.request.ParticipantRequest;

public final class ParticipantMapper {

    public static Participant toModel(ParticipantRequest request) {
        return new Participant(
                request.getName(),
                request.getPaid(),
                BigDecimal.ZERO
        );
    }
}
