package com.pm.patientservice.service;

import com.pm.patientservice.exceptions.EmailAlreadyExistsException;
import com.pm.patientservice.kafka.KfkaProducer;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import com.pm.patientservice.service.dto.PatientRequestDTO;
import com.pm.patientservice.service.dto.PatientResponseDTO;
import com.pm.patientservice.service.mapper.PatientMapper;
import com.pm.patientservice.client.BillingServiceGrpcClient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private PatientRepository patientRepository;
    private BillingServiceGrpcClient billingServiceGrpcClient;
    private final KfkaProducer producer;
    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient, KfkaProducer producer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.producer = producer;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(this::convertToDTO).toList();
    }

    private PatientResponseDTO convertToDTO(Patient patient) {
        return PatientMapper.convertToDTO(patient);
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Patient with email " + patientRequestDTO.getEmail() + " already exists.");
        }
        Patient patient = PatientMapper.convertToModel(patientRequestDTO);
        Patient savedPatient = patientRepository.save(patient);
        billingServiceGrpcClient.createBillingAccount(savedPatient.getId().toString(), savedPatient.getName(), savedPatient.getEmail());

        producer.sendMessage(savedPatient);
        return convertToDTO(savedPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {

        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Patient with email " + patientRequestDTO.getEmail() + " already exists.");
        }

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id " + id));
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(java.time.LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        Patient updatedPatient = patientRepository.save(patient);
        return convertToDTO(updatedPatient);
    }

}
