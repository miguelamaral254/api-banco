package br.com.apibanco.domain.services;

import br.com.apibanco.domain.DTOs.CreateTransactionDTO;
import br.com.apibanco.domain.DTOs.TransactionResponseDTO;
import br.com.apibanco.domain.enums.ErrorCodeEnum;
import br.com.apibanco.domain.exceptions.BusinessException;
import br.com.apibanco.domain.models.Account;
import br.com.apibanco.domain.models.CheckingAccount;
import br.com.apibanco.domain.models.SavingsAccount;
import br.com.apibanco.domain.models.Transaction;
import br.com.apibanco.domain.repositories.CheckingAccountRepository;
import br.com.apibanco.domain.repositories.SavingsAccountRepository;
import br.com.apibanco.domain.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    public TransactionResponseDTO createTransaction(CreateTransactionDTO transactionDTO) {
        if (transactionDTO == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        Account account = findAccountByNumber(transactionDTO.accountNumber());

        Account targetAccount = null;
        if (transactionDTO.targetAccountNumber() > 0) {
            targetAccount = findAccountByNumber(transactionDTO.targetAccountNumber());
        }

        if (account.getBalance() < transactionDTO.amount()) {
            throw new BusinessException(ErrorCodeEnum.INSUFFICIENT_BALANCE);
        }

        Transaction transaction = new Transaction();
        transaction.setType(transactionDTO.type());
        transaction.setDate(transactionDTO.date());
        transaction.setAmount(transactionDTO.amount());
        transaction.setAccount(account);
        transaction.setTargetAccount(targetAccount);
        transaction.setValueType(transactionDTO.valueType());

        account.setBalance(account.getBalance() - transactionDTO.amount());

        if (targetAccount != null) {
            targetAccount.setBalance(targetAccount.getBalance() + transactionDTO.amount());
        }

        transactionRepository.save(transaction);
        saveAccount(account);
        if (targetAccount != null) {
            saveAccount(targetAccount);
        }

        return new TransactionResponseDTO(transaction);
    }

    private Account findAccountByNumber(int number) {
        Optional<CheckingAccount> checkingAccount = checkingAccountRepository.findByNumber(number);
        if (checkingAccount.isPresent()) {
            return checkingAccount.get();
        }

        Optional<SavingsAccount> savingsAccount = savingsAccountRepository.findByNumber(number);
        if (savingsAccount.isPresent()) {
            return savingsAccount.get();
        }

        throw new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND);
    }

    private void saveAccount(Account account) {
        if (account instanceof CheckingAccount) {
            checkingAccountRepository.save((CheckingAccount) account);
        } else if (account instanceof SavingsAccount) {
            savingsAccountRepository.save((SavingsAccount) account);
        } else {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
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
