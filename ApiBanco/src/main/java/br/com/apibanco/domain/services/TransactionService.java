package br.com.apibanco.domain.services;

import br.com.apibanco.domain.DTOs.AccountResponseDTO;
import br.com.apibanco.domain.DTOs.CreateTransactionDTO;
import br.com.apibanco.domain.DTOs.TransactionResponseDTO;
import br.com.apibanco.domain.exceptions.BusinessException;
import br.com.apibanco.domain.models.Account;
import br.com.apibanco.domain.models.Transaction;
import br.com.apibanco.domain.enums.ErrorCodeEnum;
import br.com.apibanco.domain.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CheckingAccountService checkingAccountService;

    @Autowired
    private SavingsAccountService savingsAccountService;

    public TransactionResponseDTO createTransaction(CreateTransactionDTO transactionDTO) {
        if (transactionDTO == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        // Buscar a conta de origem pelo número usando o serviço genérico
        Account account = findAccountByNumber(transactionDTO.accountNumber());

        // Buscar a conta de destino pelo número, se necessário
        Account targetAccount = null;
        if (transactionDTO.targetAccountNumber() > 0) {
            targetAccount = findAccountByNumber(transactionDTO.targetAccountNumber());
        }

        // Criar a transação
        Transaction transaction = new Transaction();
        transaction.setType(transactionDTO.type());
        transaction.setDate(transactionDTO.date());
        transaction.setAmount(transactionDTO.amount());
        transaction.setAccount(account);
        transaction.setTargetAccount(targetAccount);
        transaction.setValueType(transactionDTO.valueType());

        transaction = transactionRepository.save(transaction);

        // Converter para DTO antes de retornar
        return new TransactionResponseDTO(transaction);
    }

    private Account findAccountByNumber(int number) {
        Account account = checkingAccountService.getAllAccounts().stream()
                .filter(a -> a.number() == number)
                .findFirst()
                .map(AccountResponseDTO::toEntity)
                .orElse(null);

        if (account == null) {
            account = savingsAccountService.getAllAccounts().stream()
                    .filter(a -> a.number() == number)
                    .findFirst()
                    .map(AccountResponseDTO::toEntity)
                    .orElse(null);
        }

        if (account == null) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND);
        }

        return account;
    }

    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(TransactionResponseDTO::new)
                .collect(Collectors.toList());
    }

    public TransactionResponseDTO getTransactionById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.TRANSACTION_NOT_FOUND));

        return new TransactionResponseDTO(transaction);
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.TRANSACTION_NOT_FOUND));

        transactionRepository.delete(transaction);
    }
}
