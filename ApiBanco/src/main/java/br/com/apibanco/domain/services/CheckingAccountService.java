package br.com.apibanco.domain.services;


import br.com.apibanco.domain.exceptions.BusinessException;
import br.com.apibanco.domain.models.CheckingAccount;
import br.com.apibanco.domain.enums.ErrorCodeEnum;
import br.com.apibanco.domain.repositories.CheckingAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckingAccountService {

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    public CheckingAccount createCheckingAccount(CheckingAccount checkingAccount) {
        return checkingAccountRepository.save(checkingAccount);
    }

    public List<CheckingAccount> getAllCheckingAccounts() {
        return checkingAccountRepository.findAll();
    }

    public CheckingAccount getCheckingAccountById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        return checkingAccountRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND));
    }

    public void deleteCheckingAccount(Long id) {
        CheckingAccount checkingAccount = getCheckingAccountById(id);
        checkingAccountRepository.delete(checkingAccount);
    }
}
