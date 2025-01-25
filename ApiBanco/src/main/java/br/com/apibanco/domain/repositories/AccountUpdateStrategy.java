package br.com.apibanco.domain.repositories;

import br.com.apibanco.domain.models.Account;

import java.util.Map;

public interface AccountUpdateStrategy<T extends Account> {
    void update(T account, Map<String, Object> updates);
}
