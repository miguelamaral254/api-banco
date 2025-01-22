package br.com.apibanco.domain.services;

import br.com.apibanco.domain.DTOs.CreateAccountDTO;
import br.com.apibanco.domain.DTOs.CustomerDTO;
import br.com.apibanco.domain.enums.ErrorCodeEnum;
import br.com.apibanco.domain.exceptions.BusinessException;
import br.com.apibanco.domain.models.Account;
import br.com.apibanco.domain.models.Address;
import br.com.apibanco.domain.models.Agency;
import br.com.apibanco.domain.models.Customer;
import br.com.apibanco.domain.repositories.AccountRepository;
import br.com.apibanco.domain.repositories.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AccountService<T extends Account> {

    private final AccountRepository<T> accountRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AgencyRepository agencyRepository;

    protected AccountService(AccountRepository<T> accountRepository) {
        this.accountRepository = accountRepository;
    }

    public T createAccount(CreateAccountDTO createAccountDTO) {
        if (createAccountDTO == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        Agency agency = agencyRepository.findByNumber(createAccountDTO.agencyNumber())
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.AGENCY_NOT_FOUND));

        CustomerDTO customerDTO = createAccountDTO.customer();
        Customer customer;

        if (customerDTO.id() == null) {
            if (customerDTO.address() != null) {
                Address address = addressService.createAddress(addressService.toEntity(customerDTO.address()));
                customerDTO = new CustomerDTO(
                        customerDTO.id(),
                        customerDTO.name(),
                        customerDTO.cpf(),
                        customerDTO.birthDate(),
                        customerDTO.rg(),
                        customerDTO.email(),
                        customerDTO.phone(),
                        addressService.toDTO(address)
                );
            }
            customerDTO = customerService.createCustomer(customerDTO);
        }

        customer = customerService.toEntity(customerDTO);

        T account = getEntityInstance();
        account.setCustomer(customer);
        account.setAgency(agency);
        account.setNumber(createAccountDTO.number());
        account.setCreationDate(createAccountDTO.creationDate());
        account.setBalance(createAccountDTO.balance());
        account.setStatus(createAccountDTO.status());

        try {
            return accountRepository.save(account);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new BusinessException(ErrorCodeEnum.DUPLICATE_ACCOUNT_NUMBER);
        }
    }

    public List<T> getAllAccounts() {
        List<T> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND);
        }
        return accounts;
    }

    public T getAccountById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        return accountRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.ACCOUNT_NOT_FOUND));
    }

    public T updateAccount(Long id, T updatedAccount) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        T existingAccount = getAccountById(id);
        existingAccount.setCustomer(updatedAccount.getCustomer());
        existingAccount.setNumber(updatedAccount.getNumber());
        existingAccount.setAgency(updatedAccount.getAgency());
        existingAccount.setBalance(updatedAccount.getBalance());
        existingAccount.setStatus(updatedAccount.isStatus());
        existingAccount.setTransactions(updatedAccount.getTransactions());

        return accountRepository.save(existingAccount);
    }

    public void deleteAccount(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        T account = getAccountById(id);
        accountRepository.delete(account);
    }

    protected abstract T getEntityInstance();
}
