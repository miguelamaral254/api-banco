package br.com.apibanco.domain.DTOs;

public record AgencyDTO(
        Long id,
        String name,
        int number,
        String phone,
        String email,
        AddressDTO address
) {}