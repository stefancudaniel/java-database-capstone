package com.project.back_end.services;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class DoctorService {


    DoctorRepository doctorRepository;
    AppointmentRepository appointmentRepository;
    TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public List<Appointment> getDoctorAvailability(Long doctorId, Appointment appointment) {
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, appointment.getAppointmentTime(), appointment.getEndTime());

    }

    public List<Appointment> getDoctorAvailability(Long doctorId, LocalDate date) {
        return null;

    }

    public int saveDoctor(Doctor doctor) {
        try {
            Doctor existingDoctor = doctorRepository.findByEmail(doctor.getEmail());
            if (existingDoctor != null) {
                return -1; // Doctor with the same email already exists
            }
            doctorRepository.save(doctor);
            return 1; // Doctor saved successfully
        } catch (Exception e) {
            return 0; // Internal error occurred
        }
    }

    public int updateDoctor(Doctor doctor) {
        try {
            Doctor existingDoctor = doctorRepository.findById(doctor.getId()).orElse(null);
            if (existingDoctor == null) {
                return -1; // Doctor not found
            }
            doctorRepository.save(doctor);
            return 1; // Doctor updated successfully
        } catch (Exception e) {
            return 0; // Internal error occurred
        }
    }

    @Transactional
    public List<Doctor> getDoctors() {
        return doctorRepository.findAllWithAvailableTimes();
    }

    public int deleteDoctor(Long doctorId) {
        try {
            Doctor existingDoctor = doctorRepository.findById(doctorId).orElse(null);
            if (existingDoctor == null) {
                return -1; // Doctor not found
            }
            appointmentRepository.deleteAllByDoctorId(doctorId);
            doctorRepository.deleteById(doctorId);
            return 1; // Doctor deleted successfully
        } catch (Exception e) {
            return 0; // Internal error occurred
        }
    }

    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {
        //TODO
        try {
            Doctor doctor = doctorRepository.findByEmail(login.getEmail());
            if (doctor != null) {
                if (doctor.getPassword().equals(login.getPassword())) {
                    String token = tokenService.generateToken(doctor.getEmail());
                    return ResponseEntity.ok(Map.of("token", token));
                } else {
                    return ResponseEntity.status(401).body(Map.of("message", "Invalid password."));
                }
            } else {
                return ResponseEntity.status(401).body(Map.of("message", "Doctor not found."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Internal server error: " + e.getMessage()));
        }
    }


    @Transactional
    List<Doctor> findDoctorByName(String name) {
        return doctorRepository.findByNameLike(name);
    }


    List<Doctor> filterDoctorsByNameSpecilityandTime(String name, String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return filterDoctorsByTime(doctors, amOrPm);
    }

    List<Doctor> filterDoctorByTime(String amOrPm) {
        List<Doctor> doctors = doctorRepository.findAllWithAvailableTimes();
        return filterDoctorsByTime(doctors, amOrPm);
    }

    List<Doctor> filterDoctorByNameAndTime(String name, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findByNameLike(name);
        return filterDoctorsByTime(doctors, amOrPm);
    }

    List<Doctor> filterDoctorByNameAndSpecility(String name, String specialty) {
         return doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
    }

    List<Doctor> filterDoctorByTimeAndSpecility(String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return filterDoctorsByTime(doctors, amOrPm);
    }

    List<Doctor> filterDoctorBySpecility(String specialty) {
        return doctorRepository.findBySpecialtyIgnoreCase(specialty);

    }

    List<Doctor> filterDoctorsByTime(List<Doctor> doctors, String amOrPm) {
        if (doctors == null || amOrPm == null) {
            return doctors;
        }

        String mode = amOrPm.trim().equalsIgnoreCase("PM") ? "PM" : "AM";
        java.time.LocalTime noon = java.time.LocalTime.NOON;

        return doctors.stream().filter(doctor -> {
            if (doctor == null || doctor.getAvailableTimes() == null) return false;

            for (String slot : doctor.getAvailableTimes()) {
                if (slot == null || slot.isBlank()) continue;
                String s = slot.trim();

                // Expect format "HH:mm-HH:mm" (24h). Allow spaces around hyphen.
                String[] parts = s.split("\\s*-\\s*");
                if (parts.length != 2) continue;

                try {
                    java.time.LocalTime start = java.time.LocalTime.parse(parts[0].trim());
                    java.time.LocalTime end = java.time.LocalTime.parse(parts[1].trim());

                    // Normal case: range within same day
                    if ("PM".equals(mode)) {
                        // any portion at or after 12:00 counts as PM
                        if (!end.isBefore(noon)) return true;
                    } else {
                        // any portion before 12:00 counts as AM
                        if (start.isBefore(noon)) return true;
                    }
                } catch (java.time.format.DateTimeParseException ex) {
                    // ignore invalid formats and continue
                }
            }

            return false;
        }).collect(java.util.stream.Collectors.toList());
    }

}
