package br.com.apibanco.api.controllers;

import br.com.apibanco.domain.DTOs.AccountResponseDTO;
import br.com.apibanco.domain.DTOs.CreateAccountDTO;
import br.com.apibanco.domain.DTOs.UpdateAccountDTO;
import br.com.apibanco.domain.models.Account;
import br.com.apibanco.domain.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public abstract class AccountController<T extends Account> {

    protected final AccountService<T> accountService;

    protected AccountController(AccountService<T> accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody CreateAccountDTO createAccountDTO) {
        AccountResponseDTO createdAccount = accountService.createAccount(createAccountDTO);
        return ResponseEntity.ok(createdAccount);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts() {
        List<AccountResponseDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable Long id) {
        AccountResponseDTO account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates
    ) {
        AccountResponseDTO updatedAccount = accountService.updateAccount(id, updates);
        return ResponseEntity.ok(updatedAccount);
    }
}
