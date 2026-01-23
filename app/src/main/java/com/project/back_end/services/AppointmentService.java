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
// 1. **Add @Service Annotation**:
//    - To indicate that this class is a service layer class for handling business logic.
//    - The `@Service` annotation should be added before the class declaration to mark it as a Spring service component.
//    - Instruction: Add `@Service` above the class definition.

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

// 2. **Constructor Injection for Dependencies**:
//    - The `AppointmentService` class requires several dependencies like `AppointmentRepository`, `Service`, `TokenService`, `PatientRepository`, and `DoctorRepository`.
//    - These dependencies should be injected through the constructor.
//    - Instruction: Ensure constructor injection is used for proper dependency management in Spring.



// 3. **Add @Transactional Annotation for Methods that Modify Database**:
//    - The methods that modify or update the database should be annotated with `@Transactional` to ensure atomicity and consistency of the operations.
//    - Instruction: Add the `@Transactional` annotation above methods that interact with the database, especially those modifying data.

// 4. **Book Appointment Method**:
//    - Responsible for saving the new appointment to the database.
//    - If the save operation fails, it returns `0`; otherwise, it returns `1`.
//    - Instruction: Ensure that the method handles any exceptions and returns an appropriate result code.

    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

// 5. **Update Appointment Method**:
//    - This method is used to update an existing appointment based on its ID.
//    - It validates whether the patient ID matches, checks if the appointment is available for updating, and ensures that the doctor is available at the specified time.
//    - If the update is successful, it saves the appointment; otherwise, it returns an appropriate error message.
//    - Instruction: Ensure proper validation and error handling is included for appointment updates.

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


        //appointmentRepository.save(existingAppointment);
    }

// 6. **Cancel Appointment Method**:
//    - This method cancels an appointment by deleting it from the database.
//    - It ensures the patient who owns the appointment is trying to cancel it and handles possible errors.
//    - Instruction: Make sure that the method checks for the patient ID match before deleting the appointment.

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

// 7. **Get Appointments Method**:
//    - This method retrieves a list of appointments for a specific doctor on a particular day, optionally filtered by the patient's name.
//    - It uses `@Transactional` to ensure that database operations are consistent and handled in a single transaction.
//    - Instruction: Ensure the correct use of transaction boundaries, especially when querying the database for appointments.

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

// 8. **Change Status Method**:
//    - This method updates the status of an appointment by changing its value in the database.
//    - It should be annotated with `@Transactional` to ensure the operation is executed in a single transaction.
//    - Instruction: Add `@Transactional` before this method to ensure atomicity when updating appointment status.


}
