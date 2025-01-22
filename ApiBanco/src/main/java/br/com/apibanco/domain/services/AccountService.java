package br.com.apibanco.domain.services;

import br.com.apibanco.domain.exceptions.BusinessException;
import br.com.apibanco.domain.models.Account;
import br.com.apibanco.domain.enums.ErrorCodeEnum;
import br.com.apibanco.domain.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        if (account == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND);
        }
        return accounts;
    }

    public Account getAccountById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND);
        }
        return account;
    }

    public Account updateAccount(Long id, Account updatedAccount) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        Account existingAccount = getAccountById(id);
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

        Account account = getAccountById(id);
        accountRepository.delete(account);
    }
}
