package br.com.apibanco.domain.services;

import br.com.apibanco.domain.enums.ErrorCodeEnum;
import br.com.apibanco.domain.exceptions.BusinessException;
import br.com.apibanco.domain.models.Address;
import br.com.apibanco.domain.models.Agency;
import br.com.apibanco.domain.repositories.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyService {

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private AddressService addressService;

    public Agency createAgency(Agency agency) {
        if (agency == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        Address address = addressService.createAddress(agency.getAddress());
        agency.setAddress(address);

        try {
            return agencyRepository.save(agency);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new BusinessException(ErrorCodeEnum.DUPLICATE_AGENCY);
        }
    }

    public List<Agency> getAllAgencies() {
        List<Agency> agencies = agencyRepository.findAll();
        if (agencies.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.AGENCY_NOT_FOUND);
        }
        return agencies;
    }

    public Agency getAgencyById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        return agencyRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.AGENCY_NOT_FOUND));
    }
    public Agency getAgencyByNumber(int number) {
        if (number <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        return agencyRepository.findByNumber(number)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.AGENCY_NOT_FOUND));
    }

    public Agency updateAgency(Long id, Agency updatedAgency) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        if (updatedAgency == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }

        Agency existingAgency = getAgencyById(id);

        try {
            existingAgency.setName(updatedAgency.getName());
            existingAgency.setNumber(updatedAgency.getNumber());
            existingAgency.setPhone(updatedAgency.getPhone());
            existingAgency.setEmail(updatedAgency.getEmail());

            if (updatedAgency.getAddress() != null) {
                Address address = addressService.createAddress(updatedAgency.getAddress());
                existingAgency.setAddress(address);
            }

            return agencyRepository.save(existingAgency);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new BusinessException(ErrorCodeEnum.DUPLICATE_AGENCY);
        }
    }

    public void deleteAgency(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodeEnum.INVALID_REQUEST);
        }
        Agency agency = getAgencyById(id);
        agencyRepository.delete(agency);
    }
}
