package br.com.apibanco.domain.DTOs;

import br.com.apibanco.domain.models.Transaction;
import java.util.Date;

public record TransactionResponseDTO(
        Long id,
        String type,
        Date date,
        double amount,
        int accountNumber,
        Integer targetAccountNumber,
        char valueType
) {
    public TransactionResponseDTO(Transaction transaction) {
        this(
                transaction.getId(),
                transaction.getType().toString(),
                transaction.getDate(),
                transaction.getAmount(),
                transaction.getAccount().getNumber(),
                transaction.getTargetAccount() != null ? transaction.getTargetAccount().getNumber() : null,
                transaction.getValueType()
        );
    }
}
