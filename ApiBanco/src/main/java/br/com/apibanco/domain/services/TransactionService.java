package br.com.apibanco.domain.services;

import br.com.apibanco.domain.DTOs.CreateTransactionDTO;
import br.com.apibanco.domain.exceptions.BusinessException;
import br.com.apibanco.domain.models.Account;
import br.com.apibanco.domain.models.Transaction;
import br.com.apibanco.domain.enums.ErrorCodeEnum;
import br.com.apibanco.domain.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
// AJUSTAR FALHA NO  Document nesting depth (1001)

//criar automação pra remover saldo da conta inicial pra conta final
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CheckingAccountService checkingAccountService;

    @Autowired
    private SavingsAccountService savingsAccountService;

    public Transaction createTransaction(CreateTransactionDTO transactionDTO) {
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

        return transactionRepository.save(transaction);
    }

    private Account findAccountByNumber(int number) {
        // Verificar nas contas correntes
        Account account = checkingAccountService.getAllAccounts().stream()
                .filter(a -> a.getNumber() == number)
                .findFirst()
                .orElse(null);

        // Caso não seja encontrado, verificar nas contas poupança
        if (account == null) {
            account = savingsAccountService.getAllAccounts().stream()
                    .filter(a -> a.getNumber() == number)
                    .findFirst()
                    .orElse(null);
        }

        // Se ainda não foi encontrado, lançar exceção
        if (account == null) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND);
        }

        return account;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        return transactionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.TRANSACTION_NOT_FOUND));
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = getTransactionById(id);
        transactionRepository.delete(transaction);
    }
}
