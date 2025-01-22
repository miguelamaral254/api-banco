package br.com.apibanco.domain.services;


import br.com.apibanco.domain.exceptions.BusinessException;
import br.com.apibanco.domain.models.Agency;
import br.com.apibanco.domain.enums.ErrorCodeEnum;
import br.com.apibanco.domain.repositories.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyService {

    @Autowired
    private AgencyRepository agencyRepository;

    public Agency createAgency(Agency agency) {
        return agencyRepository.save(agency);
    }

    public List<Agency> getAllAgencies() {
        return agencyRepository.findAll();
    }

    public Agency getAgencyById(Long id) {
        return agencyRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.AGENCY_NOT_FOUND.getCode(),
                        ErrorCodeEnum.AGENCY_NOT_FOUND.getMessage()));
    }

    public Agency updateAgency(Long id, Agency updatedAgency) {
        Agency existingAgency = getAgencyById(id);
        existingAgency.setName(updatedAgency.getName());
        existingAgency.setNumber(updatedAgency.getNumber());
        existingAgency.setPhone(updatedAgency.getPhone());
        existingAgency.setEmail(updatedAgency.getEmail());
        existingAgency.setAddress(updatedAgency.getAddress());
        return agencyRepository.save(existingAgency);
    }

    public void deleteAgency(Long id) {
        Agency agency = getAgencyById(id);
        agencyRepository.delete(agency);
    }
}