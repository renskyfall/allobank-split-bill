package com.github.renskyfall.splitbill.settlement.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SettlementRequest {

    @NotEmpty(message = "Participants cannot be empty")
    @Valid
    private List<ParticipantRequest> participants;

}