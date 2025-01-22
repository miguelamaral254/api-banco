package br.com.apibanco.api.controllers;

import br.com.apibanco.domain.models.SavingsAccount;
import br.com.apibanco.domain.services.SavingsAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/savings-accounts")
public class SavingsAccountController extends AccountController<SavingsAccount> {

    @Autowired
    public SavingsAccountController(SavingsAccountService savingsAccountService) {
        super(savingsAccountService);
    }
}
