package br.com.apibanco.api.controllers;

import br.com.apibanco.domain.models.CheckingAccount;
import br.com.apibanco.domain.services.CheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checking-accounts")
public class CheckingAccountController extends AccountController<CheckingAccount> {

    @Autowired
    public CheckingAccountController(CheckingAccountService checkingAccountService) {
        super(checkingAccountService);
    }
}