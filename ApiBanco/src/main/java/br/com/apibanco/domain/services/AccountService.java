package br.com.apibanco.domain.services;

import br.com.apibanco.domain.enums.ErrorCodeEnum;
import br.com.apibanco.domain.exceptions.BusinessException;
import br.com.apibanco.domain.models.Account;
import br.com.apibanco.domain.repositories.AccountRepository;

import java.util.List;

public abstract class AccountService<T extends Account> {

    private final AccountRepository<T> accountRepository;

    protected AccountService(AccountRepository<T> accountRepository) {
        this.accountRepository = accountRepository;
    }

    public T createAccount(T account) {
        if (account == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        return accountRepository.save(account);
    }

    public List<T> getAllAccounts() {
        List<T> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND);
        }
        return accounts;
    }

    public T getAccountById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        return accountRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND));
    }

    public T updateAccount(Long id, T updatedAccount) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        T existingAccount = getAccountById(id);
        existingAccount.setCustomer(updatedAccount.getCustomer());
        existingAccount.setNumber(updatedAccount.getNumber());
        existingAccount.setAgency(updatedAccount.getAgency());
        existingAccount.setBalance(updatedAccount.getBalance());
        existingAccount.setStatus(updatedAccount.isStatus());
        existingAccount.setTransactions(updatedAccount.getTransactions());

        return accountRepository.save(existingAccount);
    }

    public void deleteAccount(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        T account = getAccountById(id);
        accountRepository.delete(account);
    }
}
