package br.com.apibanco.api.controllers;

import br.com.apibanco.domain.DTOs.CreateTransactionDTO;
import br.com.apibanco.domain.DTOs.TransactionResponseDTO;
import br.com.apibanco.domain.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody CreateTransactionDTO transactionDTO) {
        TransactionResponseDTO transactionResponse = transactionService.createTransaction(transactionDTO);
        return ResponseEntity.ok(transactionResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long id) {
        TransactionResponseDTO transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }


}
