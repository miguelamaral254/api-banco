package br.com.apibanco.domain.DTOs;

import java.util.Date;

public record TransactionResponseDTO(
        Long id,
        String type,
        Date date,
        double amount,
        int accountNumber,
        int targetAccountNumber
) {}
