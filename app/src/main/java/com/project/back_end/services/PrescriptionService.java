package com.project.back_end.services;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public ResponseEntity<Map<String, String>> savePrescription(Prescription prescription) {
        try {
            if (!prescriptionRepository.findByAppointmentId(prescription.getAppointmentId()).isEmpty()) {
                return ResponseEntity.status(400).body(Map.of("message", "Prescription already exists for this appointment"));
            }
            prescriptionRepository.save(prescription);
            return ResponseEntity.status(201).body(Map.of("message", "Prescription saved successfully"));
        } catch (Exception e) {
            // Log the error
            System.err.println("Error saving prescription: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("message", "Internal server error"));
        }
    }


    public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId) {
        try {
            List<Prescription> prescriptions = prescriptionRepository.findByAppointmentId(appointmentId);
            if (prescriptions == null || prescriptions.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("message", "Prescription not found for appointment id: " + appointmentId));
            }
            Prescription prescription = prescriptions.get(0);
            return ResponseEntity.ok(Map.of("prescription", prescription));
        } catch (Exception e) {
            // Log the error
            System.err.println("Error fetching prescription: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("message", "Internal server error"));
        }
    }

    //TODO: Exception Handling and Logging Improvements


}
