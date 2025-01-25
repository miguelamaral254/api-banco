package br.com.apibanco.domain.DTOs;

public record UpdateAccountDTO(
        Double balance,
        Boolean status,
        Double interestRate
) {}
