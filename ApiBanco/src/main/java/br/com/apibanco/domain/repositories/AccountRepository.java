package br.com.apibanco.domain.repositories;

import br.com.apibanco.domain.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;


@NoRepositoryBean
public interface AccountRepository<T extends Account> extends JpaRepository<T, Long> {
    Optional<T> findByNumber(int number);
}
