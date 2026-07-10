package com.github.renskyfall.splitbill.settlement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.renskyfall.splitbill.settlement.model.dto.request.SettlementRequest;
import com.github.renskyfall.splitbill.settlement.model.dto.response.SettlementResponse;
import com.github.renskyfall.splitbill.settlement.service.SettlementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    @PostMapping
    public ResponseEntity<SettlementResponse> calculateSettlement(
            @Valid @RequestBody SettlementRequest request) {

        return ResponseEntity.ok(
                settlementService.calculateSettlement(request)
        );
    }
}
