package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("${api.path}" + "doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private Service service;


    //TODO: complete the else block
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public Object getDoctorAvailability(@PathVariable String user,
                                        @PathVariable Long doctorId,
                                        @PathVariable LocalDate date,
                                        @PathVariable String token) {
        var resp = service.validateToken(token, user);
        if (resp != null) {
            return resp;
        } else {
            return doctorService.getDoctorAvailability(doctorId, date);
        }
    }

    @GetMapping("/get")
    public Map<String, Object> getDoctor() {
        return Map.of("doctors", doctorService.getDoctors());
    }

    @PostMapping("/{token}")
    public Map<String, String> saveDoctor(@PathVariable String token,
                                          @RequestBody Doctor doctor) {
        var resp = service.validateToken(token, "admin");
        if (resp != null) {
            return Map.of("message", resp.getBody().get("message"));
        } else {
            return switch (doctorService.saveDoctor(doctor)) {
                case 1 -> Map.of("message", "Doctor added to database");
                case -1 -> Map.of("message", "Doctor already exists.");
                case 0 -> Map.of("message", "Some internal error occurred");
                default -> Map.of("message", "No idea");
            };
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }

    @PutMapping("/{token}")
    public Map<String, String> updateDoctor(@PathVariable String token,
                                            @RequestBody Doctor doctor) {
        var resp = service.validateToken(token, "admin");
        if (resp != null) {
            assert resp.getBody() != null;
            return Map.of("message", resp.getBody().get("message"));
        } else {
            return switch (doctorService.updateDoctor(doctor)) {
                case 1 -> Map.of("message", "Doctor updated successfully.");
                case 2 -> Map.of("message", "Doctor does not exist.");
                default -> Map.of("message", "Some internal error occurred");
            };
        }
    }

    @DeleteMapping("/{id}/{token}")
    public Map<String, String> deleteDoctor(@PathVariable Long doctorId,
                                            @PathVariable String token) {
        var resp = service.validateToken(token, "admin");
        if (resp != null) {
            assert resp.getBody() != null;
            return Map.of("message", resp.getBody().get("message"));
        } else {
            return switch (doctorService.deleteDoctor(doctorId)) {
                case 1 -> Map.of("message", "Doctor deleted successfully.");
                case 2 -> Map.of("message", "Doctor does not exist.");
                default -> Map.of("message", "Some internal error occurred");
            };
        }
    }

    @GetMapping("/filter/{name}/{time}/{speciality}")
    public Map<String, Object> filterDoctors(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {
        return service.filterDoctor(name, speciality, time);//Map.of("doctors", doctors);
    }

}
