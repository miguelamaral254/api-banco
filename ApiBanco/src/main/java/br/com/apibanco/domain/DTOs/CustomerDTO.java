package br.com.apibanco.domain.DTOs;

public record CustomerDTO(
        Long id,
        String name,
        String cpf,
        String birthDate,
        int rg,
        String email,
        String phone,
        AddressDTO address
) {}