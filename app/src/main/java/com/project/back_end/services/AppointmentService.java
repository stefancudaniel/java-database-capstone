package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final com.project.back_end.services.Service service;
    private final TokenService tokenService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PatientService patientService;

    public AppointmentService(AppointmentRepository appointmentRepository, com.project.back_end.services.Service service, TokenService tokenService, PatientRepository patientRepository, DoctorRepository doctorRepository, PatientService patientService) {
        this.appointmentRepository = appointmentRepository;
        this.service = service;
        this.tokenService = tokenService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.patientService = patientService;
    }

    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }


    public Map<String, String> updateAppointment(Appointment appointment) {
        Appointment existingAppointment = appointmentRepository.findById(appointment.getId()).orElse(null);
        if (existingAppointment == null) {
            return Map.of("message", "Appointment not found.");
        }

        int validationStatus = service.validateAppointment(appointment);
        if (validationStatus == 0) {
            return Map.of("message", "Doctor is not available at the requested time.");
        }
        if (validationStatus == -1) {
            return Map.of("message", "Invalid doctor ID.");
        }

        try {
            appointmentRepository.updateApponntmentTimeById(appointment.getAppointmentTime(), appointment.getId());
            return Map.of("message", "Appointment updated successfully.");
        } catch (Exception e) {
            return Map.of("message", "Error updating appointment: " + e.getMessage());
        }

    }

    public ResponseEntity<Map<String, String>> cancelAppointment(Long appointmentId, String token) {
        //TODO
        Patient pacient = (Patient) patientService.getPatientDetails(token);
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Appointment not found."));
        }
        if (!appointment.getPatient().getId().equals(pacient.getId())) {
            return ResponseEntity.status(403).body(Map.of("message", "Unauthorized to cancel this appointment."));
        }
        try {
            appointmentRepository.delete(appointment);
            return ResponseEntity.ok(Map.of("message", "Appointment canceled successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error canceling appointment: " + e.getMessage()));
        }
    }

    @Transactional
    public Map<String, Object> getAppointments(String token, String date, String patientName) {
        //TODO
        Long doctorId = doctorRepository.findByEmail(tokenService.extractIdentifier(token)).getId();
        LocalDate appointmentDate = LocalDate.parse(date);
        LocalDateTime startOfDay = appointmentDate.atStartOfDay();
        LocalDateTime endOfDay = appointmentDate.atTime(LocalTime.MAX);

        List<Appointment> appointments;
        if (patientName == null || patientName.isEmpty() || patientName.equals("null")) {
            appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, startOfDay, endOfDay);
        } else {
            appointments = appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(doctorId, patientName, startOfDay, endOfDay);
        }

        return Map.of("appointments", appointments);
    }

}
