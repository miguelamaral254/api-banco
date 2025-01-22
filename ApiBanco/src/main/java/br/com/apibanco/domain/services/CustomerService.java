package br.com.apibanco.domain.services;

import br.com.apibanco.domain.DTOs.AddressDTO;
import br.com.apibanco.domain.DTOs.CustomerDTO;
import br.com.apibanco.domain.enums.ErrorCodeEnum;
import br.com.apibanco.domain.exceptions.BusinessException;
import br.com.apibanco.domain.models.Address;
import br.com.apibanco.domain.models.Customer;
import br.com.apibanco.domain.repositories.CustomerRepository;
import br.com.apibanco.domain.services.AddressService;
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
        return customerRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.CUSTOMER_NOT_FOUND.getCode(),
                        ErrorCodeEnum.CUSTOMER_NOT_FOUND.getMessage()));
        return toDTO(customer);
    }

    public CustomerDTO getCustomerByCpf(String cpf) {
        Customer customer = customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.CUSTOMER_NOT_FOUND.getCode(),
                        ErrorCodeEnum.CUSTOMER_NOT_FOUND.getMessage()));
        return toDTO(customer);
    }


    public void createCustomer(CustomerDTO customerDTO) {
        Address address = addressService.createAddress(toEntity(customerDTO.address()));
        Customer customer = toEntity(customerDTO);
        customer.setAddress(address);
        customerRepository.save(customer);
    }

    public void updateCustomer(Long id, CustomerDTO updatedCustomerDTO) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.CUSTOMER_NOT_FOUND.getCode(),
                        ErrorCodeEnum.CUSTOMER_NOT_FOUND.getMessage()));

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

        customerRepository.save(existingCustomer);
    }


    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.CUSTOMER_NOT_FOUND.getCode(),
                        ErrorCodeEnum.CUSTOMER_NOT_FOUND.getMessage()));
        customerRepository.delete(customer);
    }

    private Customer toEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setId(dto.id());
        customer.setName(dto.name());
        customer.setCpf(dto.cpf());
        customer.setBirthDate(dto.birthDate());
        customer.setRg(dto.rg());
        customer.setEmail(dto.email());
        customer.setPhone(dto.phone());
        return customer;
    }

    private CustomerDTO toDTO(Customer customer) {
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