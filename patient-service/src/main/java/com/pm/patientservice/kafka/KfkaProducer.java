package com.pm.patientservice.kafka;

import com.pm.patientservice.model.Patient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;
import patient.events.PatientEvent;

@Service
public class KfkaProducer {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    public KfkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Patient patient) {
       PatientEvent patientEvent = PatientEvent.newBuilder()
               .setPatientId(patient.getId().toString())
               .setName(patient.getName())
               .setEmail(patient.getEmail())
               .setEventType("PATIENT_CREATED")
               .build();

         kafkaTemplate.send("patient", patientEvent.toByteArray());
    }

}

