package br.com.apibanco.domain.repositories;

import br.com.apibanco.domain.models.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
}