package br.com.apibanco.domain.DTOs;

import br.com.apibanco.domain.enums.TransactionType;

import java.util.Date;

public record TransactionDTO(
        Long id,
        TransactionType type,
        Date date,
        double amount,
        CustomerDTO transferCustomer,
        char valueType
) {}