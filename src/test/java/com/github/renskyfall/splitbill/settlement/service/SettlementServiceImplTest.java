package com.github.renskyfall.splitbill.settlement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.renskyfall.splitbill.settlement.exception.BadRequestException;
import com.github.renskyfall.splitbill.settlement.model.dto.request.ParticipantRequest;
import com.github.renskyfall.splitbill.settlement.model.dto.request.SettlementRequest;
import com.github.renskyfall.splitbill.settlement.model.dto.response.SettlementResponse;
import com.github.renskyfall.splitbill.settlement.model.dto.response.SettlementTransactionResponse;
import com.github.renskyfall.splitbill.settlement.service.serviceImpl.SettlementServiceImpl;

class SettlementServiceImplTest {

    private SettlementServiceImpl settlementService;

    @BeforeEach
    void setUp() {

        settlementService = new SettlementServiceImpl("renskyfall");

    }

    @Test
    void shouldCalculateSettlementSuccessfully() {

        // Arrange
        SettlementRequest request = new SettlementRequest();

        request.setParticipants(List.of(
                participant("Alice", 120),
                participant("Bob", 60),
                participant("Charlie", 0)
        ));

        // Act
        SettlementResponse response =
                settlementService.calculateSettlement(request);

        // Assert
        assertEquals(
                new BigDecimal(180),
                response.getTotalExpense()
        );

        assertEquals(
                new BigDecimal("60.00"),
                response.getAmountPerPerson()
        );

        assertEquals(
                1,
                response.getSettlements().size()
        );

        SettlementTransactionResponse transaction =
                response.getSettlements().get(0);

        assertEquals("Charlie", transaction.getFrom());

        assertEquals("Alice", transaction.getTo());

        assertEquals(
                new BigDecimal("60.00"),
                transaction.getAmount()
        );

    }

    @Test
    void shouldReturnNoTransactionsWhenAlreadyBalanced() {

        // Arrange
        SettlementRequest request = new SettlementRequest();

        request.setParticipants(List.of(
                participant("Alice", 50),
                participant("Bob", 50)
        ));

        // Act
        SettlementResponse response =
                settlementService.calculateSettlement(request);

        // Assert
        assertTrue(response.getSettlements().isEmpty());

    }

    @Test
    void shouldHandleSingleParticipant() {

        // Arrange
        SettlementRequest request = new SettlementRequest();

        request.setParticipants(List.of(
                participant("Alice", 100)
        ));

        // Act
        SettlementResponse response =
                settlementService.calculateSettlement(request);

        // Assert
        assertEquals(
                new BigDecimal("100.00"),
                response.getAmountPerPerson()
        );

        assertTrue(response.getSettlements().isEmpty());

    }

    @Test
    void shouldThrowBadRequestExceptionWhenParticipantsEmpty() {

        // Arrange
        SettlementRequest request = new SettlementRequest();

        request.setParticipants(Collections.emptyList());

        // Act & Assert
        assertThrows(
                BadRequestException.class,
                () -> settlementService.calculateSettlement(request)
        );

    }

    private ParticipantRequest participant(
        String name,
        int paid) {

        ParticipantRequest participant = new ParticipantRequest();

        participant.setName(name);

        participant.setPaid(BigDecimal.valueOf(paid));

        return participant;
    }

}