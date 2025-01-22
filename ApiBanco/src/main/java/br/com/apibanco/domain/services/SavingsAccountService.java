package br.com.apibanco.domain.services;

import br.com.apibanco.domain.exceptions.BusinessException;
import br.com.apibanco.domain.models.SavingsAccount;
import br.com.apibanco.domain.enums.ErrorCodeEnum;
import br.com.apibanco.domain.repositories.SavingsAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavingsAccountService {

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    public SavingsAccount createSavingsAccount(SavingsAccount savingsAccount) {
        if (savingsAccount == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        return savingsAccountRepository.save(savingsAccount);
    }

    public List<SavingsAccount> getAllSavingsAccounts() {
        List<SavingsAccount> accounts = savingsAccountRepository.findAll();
        if (accounts.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND);
        }
        return accounts;
    }

    public SavingsAccount getSavingsAccountById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        SavingsAccount savingsAccount = savingsAccountRepository.findById(id).orElse(null);
        if (savingsAccount == null) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND);
        }

        return savingsAccount;
    }

    public void deleteSavingsAccount(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        SavingsAccount savingsAccount = getSavingsAccountById(id);
        savingsAccountRepository.delete(savingsAccount);
    }
}
