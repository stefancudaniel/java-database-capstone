package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public PatientService(PatientRepository patientRepository, AppointmentRepository appointmentRepository, TokenService tokenService) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            System.out.println("Error creating patient: " + e.getMessage());
            return 0;
        }
    }

    @Transactional
    public List<AppointmentDTO> getPatientAppointment(Long patientId) {
        try {
            List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
            List<AppointmentDTO> appointmentDTOs = convertToDTO(appointments);
            return appointmentDTOs;
            //return  appointments;
        } catch (Exception e) {
            System.out.println("Error retrieving patient appointments: " + e.getMessage());
            return List.of();
        }
    }

    public List<AppointmentDTO> filterByCondition(Long patientId, String condition) {
        try {
            int status;
            if (condition.equalsIgnoreCase("future")) {
                status = 0;
            } else if (condition.equalsIgnoreCase("past")) {
                status = 1;
            } else {
                throw new IllegalArgumentException("Invalid condition. Use 'past' or 'future'.");
            }
            List<Appointment> appointments = appointmentRepository.findByPatient_IdAndStatusOrderByAppointmentTimeAsc(patientId, status);
            return convertToDTO(appointments);
        } catch (Exception e) {
            System.out.println("Error filtering appointments by condition: " + e.getMessage());
            return List.of();
        }
    }

    public List<AppointmentDTO>  filterByDoctor(Long patientId, String doctorName) {
        try {
            List<Appointment> appointments = appointmentRepository.filterByDoctorNameAndPatientId(doctorName, patientId);
            return convertToDTO(appointments);
        } catch (Exception e) {
            System.out.println("Error filtering appointments by doctor: " + e.getMessage());
            return List.of();
        }
    }

    public List<AppointmentDTO> filterByDoctorAndCondition(Long patientId, String doctorName, String condition) {
        try {
            int status;
            if (condition.equalsIgnoreCase("future")) {
                status = 0;
            } else if (condition.equalsIgnoreCase("past")) {
                status = 1;
            } else {
                throw new IllegalArgumentException("Invalid condition. Use 'past' or 'future'.");
            }

            List<Appointment> appointments = appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(doctorName, patientId, status);

            return convertToDTO(appointments);
        } catch (Exception e) {
            System.out.println("Error filtering appointments by doctor and condition: " + e.getMessage());
            return List.of();
        }
    }

    public Map<String, Object> getPatientDetails(String token) {
        try {
            String email = tokenService.extractIdentifier(token);
            Patient patient = patientRepository.findByEmail(email);
            if (patient == null) {
                return Map.of("message", "Patient not found.");
            }
            return Map.of("patient", patient);
        } catch (Exception e) {
            System.out.println("Error retrieving patient details: " + e.getMessage());
            return Map.of("message", "Error occurred while retrieving patient details.");
        }
    }


    //TODO: See above methods for examples of error handling.

    public List<AppointmentDTO> convertToDTO(List<Appointment> appointments) {
        return appointments.stream()
                .map(appointment -> new AppointmentDTO(
                        appointment.getId(),
                        appointment.getDoctor().getId(),
                        appointment.getDoctor().getName(),
                        appointment.getPatient().getId(),
                        appointment.getPatient().getName(),
                        appointment.getPatient().getEmail(),
                        appointment.getPatient().getPhone(),
                        appointment.getPatient().getAddress(),
                        appointment.getAppointmentTime(),
                        appointment.getStatus()
                ))
                .toList();
    }


}
