package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.DTO.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public Service(TokenService tokenService, AdminRepository adminRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, DoctorService doctorService, PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public ResponseEntity<Map<String, String>> validateToken(String token, String username) {
        //TODO: Implement token validation logic
        try {
            if (!tokenService.validateToken(token, username)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired token"));
            }
            return null;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred while validating the token"));
        }
    }

    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        try {
            Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());
            if (admin != null) {
                if (admin.getPassword().equals(receivedAdmin.getPassword())) {
                    String token = tokenService.generateToken(admin.getUsername());
                    return ResponseEntity.ok(Map.of("token", token));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("error", "Invalid password"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Admin not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred while validating admin credentials"));
        }
    }

    public Map<String, Object> filterDoctor(String name, String specialty, String timeSlot) {
        try {
            Map<String, Object> response;
            if (name.equals("null") && specialty.equals("null") && timeSlot.equals("null")) {
                return Map.of("doctors", doctorService.getDoctors());
            } else if (name.equals("null") && specialty.equals("null")) {
                return Map.of("doctors", doctorService.filterDoctorByTime(timeSlot));
            } else if (name.equals("null") && timeSlot.equals("null")) {
                return Map.of("doctors", doctorService.filterDoctorBySpecility(specialty));
            } else if (specialty.equals("null") && timeSlot.equals("null")) {
                return Map.of( "doctors", doctorService.findDoctorByName(name));
            } else if (name.equals("null")) {
                return Map.of( "doctors", doctorService.filterDoctorByTimeAndSpecility(specialty, timeSlot));
            } else if (specialty.equals("null")) {
                return Map.of( "doctors", doctorService.filterDoctorByNameAndTime(name, timeSlot));
            } else if (timeSlot.equals("null")) {
                return Map.of( "doctors", doctorService.filterDoctorByNameAndSpecility(name, specialty));
            } else {
                return Map.of( "doctors", doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, timeSlot));
            }
        } catch (Exception e) {
            return Map.of("error", "An error occurred while filtering doctors");
        }
    }

    //TODO
    public int validateAppointment(Appointment appointment) {
        try {
            var doctorOpt = doctorRepository.findById(appointment.getDoctor().getId());
            if (doctorOpt.isPresent()) {
                var availableSlots = doctorService.getDoctorAvailability(appointment.getDoctor().getId(), appointment);
               /* for (String slot : availableSlots) {
                    String startTime = slot.split("-")[0];
                    if (startTime.equals(appointment.getAppointmentTime().getHour() + ":" + String.format("%02d", appointment.getAppointmentTime().getMinute()))) {
                        return 1; // Valid appointment time
                    }
                }*/
                if (availableSlots.isEmpty()){
                    return 1;
                }
                return 0; // Invalid appointment time
            } else {
                return -1; // Doctor does not exist
            }
        } catch (Exception e) {
            return -1; // Error occurred
        }
    }

    Boolean validatePacient(Patient patient){
        Patient exists = patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone());
        return exists == null;
    }

    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        try {
            Patient patient = patientRepository.findByEmail(login.getEmail());
            if (patient != null) {
                if (patient.getPassword().equals(login.getPassword())) {
                    String token = tokenService.generateToken(patient.getEmail());
                    return ResponseEntity.ok(Map.of("token", token));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("error", "Invalid password"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Patient not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred while validating patient credentials"));
        }
    }


    public List<AppointmentDTO> filterPatient(String condition, String doctorName, String token) {
        Patient patient = patientRepository.findByEmail(tokenService.extractIdentifier(token));
        if (token == null) {
            return List.of();
        }
       if((!condition.equals("null")) && (!doctorName.equals("null"))){
           return patientService.filterByDoctorAndCondition(patient.getId(), doctorName, condition);
       } else if (!condition.equals("null")){
           return patientService.filterByCondition(patient.getId(), condition);
       } else {
           return patientService.filterByDoctor(patient.getId(), doctorName);
       }
    }


}
