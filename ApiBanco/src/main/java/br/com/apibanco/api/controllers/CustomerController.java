package br.com.apibanco.api.controllers;

import br.com.apibanco.domain.DTOs.CustomerDTO;
import br.com.apibanco.domain.exceptions.BusinessException;
import br.com.apibanco.domain.enums.ErrorCodeEnum;
import br.com.apibanco.domain.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<Void> createCustomer(@RequestBody CustomerDTO customerDTO) {
        if (customerDTO == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        customerService.createCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        if (customers.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.CUSTOMER_NOT_FOUND);
        }
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        CustomerDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<CustomerDTO> getCustomerByCpf(@PathVariable String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        CustomerDTO customer = customerService.getCustomerByCpf(cpf);
        return ResponseEntity.ok(customer);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        if (customerDTO == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        customerService.updateCustomer(id, customerDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
