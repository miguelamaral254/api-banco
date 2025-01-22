package br.com.apibanco.domain.repositories;

import br.com.apibanco.domain.models.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
    Optional<Agency> findByNumber(int number);
}