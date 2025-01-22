package br.com.apibanco.domain.DTOs;

import br.com.apibanco.domain.enums.State;

public record AddressDTO(
        Long id,
        State uf,
        String city,
        String neighborhood,
        String street,
        int number,
        String complement,
        String zipCode
) {}
