package br.com.apibanco.domain.services;

import br.com.apibanco.domain.models.SavingsAccount;
import br.com.apibanco.domain.repositories.SavingsAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class SavingsAccountService extends AccountService<SavingsAccount> {

    public SavingsAccountService(SavingsAccountRepository savingsAccountRepository) {
        super(savingsAccountRepository);
    }
}
