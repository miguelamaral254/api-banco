package br.com.apibanco.domain.services;

import br.com.apibanco.domain.DTOs.AddressDTO;
import br.com.apibanco.domain.DTOs.CustomerDTO;
import br.com.apibanco.domain.enums.ErrorCodeEnum;
import br.com.apibanco.domain.exceptions.BusinessException;
import br.com.apibanco.domain.models.Address;
import br.com.apibanco.domain.models.Customer;
import br.com.apibanco.domain.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressService addressService;

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.CUSTOMER_NOT_FOUND);
        }
        return customers.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.CUSTOMER_NOT_FOUND));
        return toDTO(customer);
    }

    public CustomerDTO getCustomerByCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        Customer customer = customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.CUSTOMER_NOT_FOUND));
        return toDTO(customer);
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        if (customerDTO == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        Address address = addressService.createAddress(toEntity(customerDTO.address()));
        Customer customer = toEntity(customerDTO);
        customer.setAddress(address);
        try {
            customerRepository.save(customer);
            return toDTO(customer);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new BusinessException(ErrorCodeEnum.DUPLICATE_CUSTOMER_CPF);
        }
    }

    public void updateCustomer(Long id, CustomerDTO updatedCustomerDTO) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        if (updatedCustomerDTO == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.CUSTOMER_NOT_FOUND));
        existingCustomer.setName(updatedCustomerDTO.name());
        existingCustomer.setCpf(updatedCustomerDTO.cpf());
        existingCustomer.setBirthDate(updatedCustomerDTO.birthDate());
        existingCustomer.setRg(updatedCustomerDTO.rg());
        existingCustomer.setEmail(updatedCustomerDTO.email());
        existingCustomer.setPhone(updatedCustomerDTO.phone());
        if (updatedCustomerDTO.address() != null) {
            Address address = addressService.createAddress(toEntity(updatedCustomerDTO.address()));
            existingCustomer.setAddress(address);
        }
        try {
            customerRepository.save(existingCustomer);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new BusinessException(ErrorCodeEnum.DUPLICATE_CUSTOMER_CPF_RG);
        }
    }

    public void deleteCustomer(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.CUSTOMER_NOT_FOUND));
        customerRepository.delete(customer);
    }

    public Customer toEntity(CustomerDTO dto) {
        if (dto == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(dto.id());
        customer.setName(dto.name());
        customer.setCpf(dto.cpf());
        customer.setBirthDate(dto.birthDate());
        customer.setRg(dto.rg());
        customer.setEmail(dto.email());
        customer.setPhone(dto.phone());
        customer.setAddress(toEntity(dto.address()));
        return customer;
    }

    public CustomerDTO toDTO(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getCpf(),
                customer.getBirthDate(),
                customer.getRg(),
                customer.getEmail(),
                customer.getPhone(),
                toDTO(customer.getAddress())
        );
    }

    private Address toEntity(AddressDTO dto) {
        if (dto == null) {
            return null;
        }
        Address address = new Address();
        address.setId(dto.id());
        address.setUf(dto.uf());
        address.setCity(dto.city());
        address.setNeighborhood(dto.neighborhood());
        address.setStreet(dto.street());
        address.setNumber(dto.number());
        address.setComplement(dto.complement());
        address.setZipCode(dto.zipCode());
        return address;
    }

    private AddressDTO toDTO(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressDTO(
                address.getId(),
                address.getUf(),
                address.getCity(),
                address.getNeighborhood(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getZipCode()
        );
    }
}
