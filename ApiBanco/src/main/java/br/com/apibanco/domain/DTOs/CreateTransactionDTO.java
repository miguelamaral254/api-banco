package br.com.apibanco.domain.DTOs;

import br.com.apibanco.domain.enums.TransactionType;

import java.util.Date;

public record CreateTransactionDTO(
        TransactionType type,
        Date date,
        double amount,
        int accountNumber,
        int targetAccountNumber,
        char valueType
) {}
