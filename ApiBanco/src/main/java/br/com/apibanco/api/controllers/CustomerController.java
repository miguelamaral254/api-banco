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



}
