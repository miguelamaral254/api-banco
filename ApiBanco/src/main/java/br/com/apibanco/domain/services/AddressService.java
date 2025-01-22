package br.com.apibanco.domain.services;

import br.com.apibanco.domain.enums.ErrorCodeEnum;
import br.com.apibanco.domain.exceptions.BusinessException;
import br.com.apibanco.domain.models.Address;
import br.com.apibanco.domain.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Address getAddressById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        Address address = addressRepository.findById(id).orElse(null);

        if (address == null) {
            throw new BusinessException(ErrorCodeEnum.ADDRESS_NOT_FOUND);
        }

        return address;
    }


    public Address updateAddress(Long id, Address updatedAddress) {
        Address existingAddress = getAddressById(id);
        existingAddress.setUf(updatedAddress.getUf());
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setNeighborhood(updatedAddress.getNeighborhood());
        existingAddress.setStreet(updatedAddress.getStreet());
        existingAddress.setNumber(updatedAddress.getNumber());
        existingAddress.setComplement(updatedAddress.getComplement());
        existingAddress.setZipCode(updatedAddress.getZipCode());
        return addressRepository.save(existingAddress);
    }

    public void deleteAddress(Long id) {
        Address address = getAddressById(id);
        addressRepository.delete(address);
    }
}
