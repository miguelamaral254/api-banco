package br.com.apibanco.domain.services;

import br.com.apibanco.domain.DTOs.AccountResponseDTO;
import br.com.apibanco.domain.DTOs.CreateAccountDTO;
import br.com.apibanco.domain.DTOs.UpdateAccountDTO;
import br.com.apibanco.domain.enums.ErrorCodeEnum;
import br.com.apibanco.domain.exceptions.BusinessException;
import br.com.apibanco.domain.models.Account;
import br.com.apibanco.domain.models.Agency;
import br.com.apibanco.domain.models.SavingsAccount;
import br.com.apibanco.domain.repositories.AccountRepository;
import br.com.apibanco.domain.repositories.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class AccountService<T extends Account> {

    private final AccountRepository<T> accountRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    protected AccountService(AccountRepository<T> accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponseDTO createAccount(CreateAccountDTO createAccountDTO) {
        if (createAccountDTO == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        Agency agency = agencyRepository.findByNumber(createAccountDTO.agencyNumber())
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.AGENCY_NOT_FOUND));

        T account = getEntityInstance();
        account.setAgency(agency);
        account.setNumber(createAccountDTO.number());
        account.setCreationDate(createAccountDTO.creationDate());
        account.setBalance(createAccountDTO.balance());
        account.setStatus(createAccountDTO.status());

        try {
            return new AccountResponseDTO(accountRepository.save(account));
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new BusinessException(ErrorCodeEnum.DUPLICATE_ACCOUNT_NUMBER);
        }
    }

    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(AccountResponseDTO::new)
                .collect(Collectors.toList());
    }

    public AccountResponseDTO getAccountById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        T account = accountRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND));

        return new AccountResponseDTO(account);
    }

    public AccountResponseDTO updateAccount(Long id, Map<String, Object> updates) {
        if (id == null || id <= 0 || updates == null || updates.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        T account = accountRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND));

        Map<String, Consumer<Object>> updateActions = Map.of(
                "balance", value -> account.setBalance((Double) value),
                "status", value -> account.setStatus((Boolean) value),
                "interestRate", value -> {
                    if (account instanceof SavingsAccount savingsAccount) {
                        savingsAccount.setInterestRate((Double) value);
                    } else {
                        throw new BusinessException(ErrorCodeEnum.INVALID_INTEREST_RATE_UPDATE);
                    }
                }
        );

        updates.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .filter(entry -> updateActions.containsKey(entry.getKey()))
                .forEach(entry -> updateActions.get(entry.getKey()).accept(entry.getValue()));

        try {
            return new AccountResponseDTO(accountRepository.save(account));
        } catch (Exception ex) {
            throw new BusinessException(ErrorCodeEnum.DUPLICATE_ACCOUNT_NUMBER);
        }
    }

    public T findAccountByNumber(int number) {
        return accountRepository.findAll().stream()
                .filter(account -> account.getNumber() == number)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND));
    }

    public void saveAccount(T account) {
        try {
            accountRepository.save(account);
        } catch (Exception ex) {
            throw new BusinessException(ErrorCodeEnum.ERROR_WHILE_SAVING_ACCOUNT);
        }
    }

    protected abstract T getEntityInstance();
}
