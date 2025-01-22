package br.com.apibanco.api.controllers;
import br.com.apibanco.domain.models.Agency;
import br.com.apibanco.domain.services.AgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agencies")
public class AgencyController {

    @Autowired
    private AgencyService agencyService;

    @PostMapping
    public ResponseEntity<Agency> createAgency(@RequestBody Agency agency) {
        return ResponseEntity.ok(agencyService.createAgency(agency));
    }
    @GetMapping("/number/{number}")
    public ResponseEntity<Agency> getAgencyByNumber(@PathVariable int number) {
        return ResponseEntity.ok(agencyService.getAgencyByNumber(number));
    }

    @GetMapping
    public ResponseEntity<List<Agency>> getAllAgencies() {
        return ResponseEntity.ok(agencyService.getAllAgencies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agency> getAgencyById(@PathVariable Long id) {
        return ResponseEntity.ok(agencyService.getAgencyById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agency> updateAgency(@PathVariable Long id, @RequestBody Agency agency) {
        return ResponseEntity.ok(agencyService.updateAgency(id, agency));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgency(@PathVariable Long id) {
        agencyService.deleteAgency(id);
        return ResponseEntity.noContent().build();
    }
}