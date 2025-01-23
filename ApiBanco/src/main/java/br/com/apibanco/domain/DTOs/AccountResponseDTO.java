package br.com.apibanco.domain.DTOs;

import br.com.apibanco.domain.models.Account;
import br.com.apibanco.domain.models.CheckingAccount;
import br.com.apibanco.domain.models.SavingsAccount;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public record AccountResponseDTO(
        Long id,
        int number,
        Long agency,
        Date creationDate,
        double balance,
        boolean status,
        List<TransactionResponseDTO> transactions,
        String accountType
) {
    public AccountResponseDTO(Account account) {
        this(
                account.getId(),
                account.getNumber(),
                account.getAgency() != null ? account.getAgency().getId() : null,
                account.getCreationDate(),
                account.getBalance(),
                account.isStatus(),
                account.getTransactions().stream()
                        .map(TransactionResponseDTO::new)
                        .collect(Collectors.toList()),
                account.getClass().getSimpleName()
        );
    }

    public Account toEntity() {
        Account account;
        if ("CheckingAccount".equals(accountType)) {
            account = new CheckingAccount();
        } else if ("SavingsAccount".equals(accountType)) {
            account = new SavingsAccount();
        } else {
            throw new IllegalArgumentException("Tipo de conta desconhecido: " + accountType);
        }

        account.setId(this.id());
        account.setNumber(this.number());
        account.setBalance(this.balance());
        account.setStatus(this.status());
        account.setCreationDate(this.creationDate());
        return account;
    }
}
