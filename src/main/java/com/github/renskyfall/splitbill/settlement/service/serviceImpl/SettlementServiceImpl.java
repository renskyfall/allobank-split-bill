package com.github.renskyfall.splitbill.settlement.service.serviceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.renskyfall.splitbill.settlement.exception.BadRequestException;
import com.github.renskyfall.splitbill.settlement.model.Transaction;
import com.github.renskyfall.splitbill.settlement.model.dto.Participant;
import com.github.renskyfall.splitbill.settlement.model.dto.request.SettlementRequest;
import com.github.renskyfall.splitbill.settlement.model.dto.response.SettlementResponse;
import com.github.renskyfall.splitbill.settlement.model.dto.response.SettlementTransactionResponse;
import com.github.renskyfall.splitbill.settlement.service.SettlementService;
import com.github.renskyfall.splitbill.settlement.util.ParticipantMapper;
import com.github.renskyfall.splitbill.settlement.util.SettlementUtil;

@Service
public class SettlementServiceImpl implements SettlementService {

    private final String githubUsername;

    public SettlementServiceImpl(
            @Value("${app.github.username}") String githubUsername) {

        this.githubUsername = githubUsername;
    }
    
    @Override
    public SettlementResponse calculateSettlement(SettlementRequest request) {

        validateRequest(request);

        List<Participant> participants = mapParticipants(request);

        BigDecimal totalExpense = calculateTotalExpense(participants);

        BigDecimal amountPerPerson = calculateAmountPerPerson(
                totalExpense,
                participants.size()
        );

        calculateBalances(participants, amountPerPerson);

        List<Transaction> transactions = generateTransactions(participants);

        int serviceChargePct =
                SettlementUtil.calculateServiceChargePercentage(githubUsername);

        BigDecimal serviceChargeAmount =
                SettlementUtil.calculateServiceChargeAmount(
                        totalExpense,
                        githubUsername
                );

        return buildResponse(
                totalExpense,
                amountPerPerson,
                serviceChargePct,
                serviceChargeAmount,
                transactions
        );
    }

    private void validateRequest(SettlementRequest request) {

        if (request.getParticipants() == null || request.getParticipants().isEmpty()) {
            throw new BadRequestException("Participants cannot be empty.");
        }

    }

    private List<Participant> mapParticipants(SettlementRequest request) {

        return request.getParticipants()
                .stream()
                .map(ParticipantMapper::toModel)
                .toList();

    }

    private BigDecimal calculateTotalExpense(List<Participant> participants) {

        return participants.stream()
                .map(Participant::getPaid)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    private BigDecimal calculateAmountPerPerson(
            BigDecimal totalExpense,
            int participantCount) {

        return totalExpense.divide(
                BigDecimal.valueOf(participantCount),
                2,
                RoundingMode.HALF_UP
        );

    }

    private void calculateBalances(
            List<Participant> participants,
            BigDecimal amountPerPerson) {

        participants.forEach(participant ->
                participant.setBalance(
                        participant.getPaid().subtract(amountPerPerson)
                )
        );

    }

    private List<Transaction> generateTransactions(List<Participant> participants) {

        List<Participant> creditors = participants.stream()
        .filter(p -> p.getBalance().compareTo(BigDecimal.ZERO) > 0)
        .toList();

        List<Participant> debtors = participants.stream()
        .filter(p -> p.getBalance().compareTo(BigDecimal.ZERO) < 0)
        .toList();

        List<Transaction> transactions = new ArrayList<>();

        int creditorIndex = 0;
        int debtorIndex = 0;

        while (creditorIndex < creditors.size()
                && debtorIndex < debtors.size()) {

            Participant creditor = creditors.get(creditorIndex);
            Participant debtor = debtors.get(debtorIndex);

            BigDecimal amount = creditor.getBalance()
            .min(debtor.getBalance().abs());

            transactions.add(
                new Transaction(
                    debtor.getName(),
                    creditor.getName(),
                    amount
                )
            );

            creditor.setBalance(
                creditor.getBalance().subtract(amount)
            );

            debtor.setBalance(
                debtor.getBalance().add(amount)
            );

            if (creditor.getBalance().compareTo(BigDecimal.ZERO) == 0) {
                creditorIndex++;
            }

            if (debtor.getBalance().compareTo(BigDecimal.ZERO) == 0) {
                debtorIndex++;
            }
        }

        return transactions;

    }

    private SettlementResponse buildResponse(
            BigDecimal totalExpense,
            BigDecimal amountPerPerson,
            Integer serviceChargePct,
            BigDecimal serviceChargeAmount,
            List<Transaction> transactions) {

        return SettlementResponse.builder()
                .totalExpense(totalExpense)
                .amountPerPerson(amountPerPerson)
                .serviceChargePct(serviceChargePct)
                .serviceChargeAmount(serviceChargeAmount)
                .settlements(
                        transactions.stream()
                                .map(transaction ->
                                        SettlementTransactionResponse.builder()
                                                .from(transaction.getFrom())
                                                .to(transaction.getTo())
                                                .amount(transaction.getAmount())
                                                .build()
                                )
                                .toList()
                )
                .build();

    }
    
}
