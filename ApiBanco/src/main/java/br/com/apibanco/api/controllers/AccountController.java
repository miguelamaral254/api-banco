package br.com.apibanco.api.controllers;

import br.com.apibanco.domain.exceptions.BusinessException;
import br.com.apibanco.domain.models.Account;
import br.com.apibanco.domain.enums.ErrorCodeEnum;
import br.com.apibanco.domain.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class AccountController<T extends Account> {

    protected final AccountService<T> accountService;

    protected AccountController(AccountService<T> accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<T> createAccount(@RequestBody T account) {
        if (account == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    @GetMapping
    public ResponseEntity<List<T>> getAllAccounts() {
        List<T> accounts = accountService.getAllAccounts();
        if (accounts.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND);
        }
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> getAccountById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        T account = accountService.getAccountById(id);
        if (account == null) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND);
        }
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> updateAccount(@PathVariable Long id, @RequestBody T account) {
        if (id == null || id <= 0 || account == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        return ResponseEntity.ok(accountService.updateAccount(id, account));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
