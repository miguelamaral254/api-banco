package br.com.apibanco.api.controllers;

import br.com.apibanco.domain.models.CheckingAccount;
import br.com.apibanco.domain.services.CheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checking-accounts")
public class CheckingAccountController {

    @Autowired
    private CheckingAccountService checkingAccountService;

    @PostMapping
    public ResponseEntity<CheckingAccount> createCheckingAccount(@RequestBody CheckingAccount checkingAccount) {
        return ResponseEntity.ok(checkingAccountService.createCheckingAccount(checkingAccount));
    }

    @GetMapping
    public ResponseEntity<List<CheckingAccount>> getAllCheckingAccounts() {
        return ResponseEntity.ok(checkingAccountService.getAllCheckingAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckingAccount> getCheckingAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(checkingAccountService.getCheckingAccountById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCheckingAccount(@PathVariable Long id) {
        checkingAccountService.deleteCheckingAccount(id);
        return ResponseEntity.noContent().build();
    }
}
