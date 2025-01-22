package br.com.apibanco.domain.services;

import br.com.apibanco.domain.models.CheckingAccount;
import br.com.apibanco.domain.repositories.CheckingAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class CheckingAccountService extends AccountService<CheckingAccount> {

    public CheckingAccountService(CheckingAccountRepository checkingAccountRepository) {
        super(checkingAccountRepository);
    }
}
