package br.com.apibanco.domain.repositories;


import br.com.apibanco.domain.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}