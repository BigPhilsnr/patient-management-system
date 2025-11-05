package com.pm.patientservice.controller;

import com.pm.patientservice.service.PatientService;
import com.pm.patientservice.service.dto.PatientRequestDTO;
import com.pm.patientservice.service.dto.PatientResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patient Management", description = "APIs for managing patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientRepository) {
        this.patientService = patientRepository;
    }

    @GetMapping
    @Operation(summary = "Get all patients", description = "Retrieve a list of all patients")
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        List<PatientResponseDTO> patients = patientService.getPatients();
        return ResponseEntity.ok(patients);
    }

    @PostMapping
    @Operation(summary = "Create a new patient", description = "Create a new patient with the provided details")
    public  ResponseEntity<PatientResponseDTO> savePatient(@Valid @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO savedPatient = patientService.createPatient(patientRequestDTO);
        return ResponseEntity.ok(savedPatient);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing patient", description = "Update the details of an existing patient by ID")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable String id, @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO updatedPatient = patientService.updatePatient(java.util.UUID.fromString(id), patientRequestDTO);
        return ResponseEntity.ok(updatedPatient);
    }
}
