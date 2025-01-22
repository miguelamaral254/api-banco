package br.com.apibanco.domain.services;

import br.com.apibanco.domain.models.Agency;
import br.com.apibanco.domain.repositories.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AgencyService {

    @Autowired
    private AgencyRepository agencyRepository;

    public Agency createAgency(Agency agency) {
        return agencyRepository.save(agency);
    }

    public List<Agency> getAllAgencies() {
        List<Agency> agencies = agencyRepository.findAll();
        if (agencies.isEmpty()) {
            throw new NoSuchElementException("No agencies found.");
        }
        return agencies;
    }

    public Agency getAgencyById(Long id) {
        return agencyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Agency not found with ID: " + id));
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
