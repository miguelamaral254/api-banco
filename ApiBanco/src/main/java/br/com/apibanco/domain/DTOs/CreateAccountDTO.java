package br.com.apibanco.domain.DTOs;

import java.util.Date;

public record CreateAccountDTO(
        CustomerDTO customer,
        int number,
        int agencyNumber,
        Date creationDate,
        double balance,
        boolean status,
        double interestRate
) {}
