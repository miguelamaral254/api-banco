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
        validateCreateAccountDTO(createAccountDTO);

        Agency agency = findAgency(createAccountDTO.agencyNumber());
        T account = initializeAccount(createAccountDTO, agency);

        return saveAccount(account);
    }

    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(AccountResponseDTO::new)
                .collect(Collectors.toList());
    }

    public AccountResponseDTO getAccountById(Long id) {
        validateId(id);
        T account = findAccountById(id);
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

    protected abstract T getEntityInstance();

    // --- Métodos Auxiliares para SRP ---

    private void validateCreateAccountDTO(CreateAccountDTO createAccountDTO) {
        if (createAccountDTO == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
    }

    private void validateUpdateAccountDTO(Long id, UpdateAccountDTO updateAccountDTO) {
        if (id == null || id <= 0 || updateAccountDTO == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
    }

    private Agency findAgency(int agencyNumber) {
        return agencyRepository.findByNumber(agencyNumber)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.AGENCY_NOT_FOUND));
    }

    private T findAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND));
    }

    private T initializeAccount(CreateAccountDTO createAccountDTO, Agency agency) {
        T account = getEntityInstance();
        account.setAgency(agency);
        account.setNumber(createAccountDTO.number());
        account.setCreationDate(createAccountDTO.creationDate());
        account.setBalance(createAccountDTO.balance());
        account.setStatus(createAccountDTO.status());
        return account;
    }

    private void performUpdates(T account, UpdateAccountDTO updateAccountDTO) {
        Map<String, Consumer<Object>> updateActions = Map.of(
                "balance", value -> account.setBalance((Double) value),
                "status", value -> account.setStatus((Boolean) value),
                "interestRate", value -> {
                    if (account instanceof SavingsAccount savingsAccount) {
                        savingsAccount.setInterestRate((Double) value);
                    }
                }
        );

        Map<String, Object> dtoValues = Map.of(
                "balance", updateAccountDTO.balance(),
                "status", updateAccountDTO.status(),
                "interestRate", updateAccountDTO.interestRate()
        );

        dtoValues.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .filter(entry -> updateActions.containsKey(entry.getKey()))
                .forEach(entry -> updateActions.get(entry.getKey()).accept(entry.getValue()));
    }

    private AccountResponseDTO saveAccount(T account) {
        try {
            return new AccountResponseDTO(accountRepository.save(account));
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new BusinessException(ErrorCodeEnum.DUPLICATE_ACCOUNT_NUMBER);
        }
    }
}
