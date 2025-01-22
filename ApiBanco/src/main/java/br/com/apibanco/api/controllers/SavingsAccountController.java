package br.com.apibanco.api.controllers;


import br.com.apibanco.domain.models.SavingsAccount;
import br.com.apibanco.domain.services.SavingsAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/savings-accounts")
public class SavingsAccountController {

    @Autowired
    private SavingsAccountService savingsAccountService;

    @PostMapping
    public ResponseEntity<SavingsAccount> createSavingsAccount(@RequestBody SavingsAccount savingsAccount) {
        return ResponseEntity.ok(savingsAccountService.createSavingsAccount(savingsAccount));
    }

    @GetMapping
    public ResponseEntity<List<SavingsAccount>> getAllSavingsAccounts() {
        return ResponseEntity.ok(savingsAccountService.getAllSavingsAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavingsAccount> getSavingsAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(savingsAccountService.getSavingsAccountById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSavingsAccount(@PathVariable Long id) {
        savingsAccountService.deleteSavingsAccount(id);
        return ResponseEntity.noContent().build();
    }
}
