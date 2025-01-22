package br.com.apibanco.domain.repositories;


import br.com.apibanco.domain.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}