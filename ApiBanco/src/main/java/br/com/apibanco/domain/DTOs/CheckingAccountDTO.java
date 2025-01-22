package br.com.apibanco.domain.DTOs;


import java.util.Date;
import java.util.List;

public record CheckingAccountDTO(
        Long id,
        CustomerDTO customer,
        int number,
        AgencyDTO agency,
        Date creationDate,
        double balance,
        boolean status,
        List<TransactionDTO> transactions,
        double interestRate
) {}