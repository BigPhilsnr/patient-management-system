package com.pm.patientservice.service.mapper;

import com.pm.patientservice.model.Patient;
import com.pm.patientservice.service.dto.PatientRequestDTO;
import com.pm.patientservice.service.dto.PatientResponseDTO;

public class PatientMapper {
    public static PatientResponseDTO convertToDTO(Patient patient) {

        PatientResponseDTO dto = new PatientResponseDTO();
        dto.setId(patient.getId().toString());
        dto.setName(patient.getName());
        dto.setEmail(patient.getEmail());
        dto.setAddress(patient.getAddress());
        dto.setDateOfBirth(patient.getDateOfBirth().toString());
        dto.setRegisteredDate(patient.getRegisteredDate().toString());
        return dto;
    }

    public static Patient convertToModel(PatientRequestDTO dto) {
        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setAddress(dto.getAddress());
        patient.setDateOfBirth(java.time.LocalDate.parse(dto.getDateOfBirth()));
        patient.setRegisteredDate(java.time.LocalDate.parse(dto.getRegisteredDate()));
        return patient;
    }
}
