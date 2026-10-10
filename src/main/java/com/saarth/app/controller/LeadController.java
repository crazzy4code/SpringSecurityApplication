package com.saarth.app.controller;

import com.saarth.app.persistance.entity.Lead;
import com.saarth.app.persistance.repository.LeadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadRepository leadRepository;

    @GetMapping
    public List<Lead> getAllLeads() {
        return leadRepository.findAll();
    }

    @GetMapping("/{leadId}")
    public Lead getLeadById(@PathVariable UUID leadId) {
        return leadRepository.findById(leadId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Lead not found with id: " + leadId));
    }

    @PostMapping
    public ResponseEntity<Lead> createLead(@RequestBody Lead lead) {
        if (lead == null || lead.getName() == null || lead.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lead name is required");
        }

        Instant now = Instant.now();
        Lead newLead = Lead.builder()
                .name(lead.getName())
                .email(lead.getEmail())
                .phone(lead.getPhone())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Lead savedLead = leadRepository.save(newLead);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLead);
    }

    @PutMapping("/{leadId}")
    public Lead updateLead(@PathVariable UUID leadId, @RequestBody Lead updatedLead) {
        Lead existingLead = leadRepository.findById(leadId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Lead not found with id: " + leadId));

        if (updatedLead == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lead payload is required");
        }

        if (updatedLead.getName() == null || updatedLead.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lead name is required");
        }

        existingLead.setName(updatedLead.getName());
        existingLead.setEmail(updatedLead.getEmail());
        existingLead.setPhone(updatedLead.getPhone());
        existingLead.setUpdatedAt(Instant.now());

        return leadRepository.save(existingLead);
    }

    @DeleteMapping("/{leadId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLead(@PathVariable UUID leadId) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Lead not found with id: " + leadId));

        leadRepository.delete(lead);
    }
}
