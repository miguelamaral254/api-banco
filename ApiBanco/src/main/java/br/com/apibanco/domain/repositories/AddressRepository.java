package br.com.apibanco.domain.repositories;

import br.com.apibanco.domain.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}