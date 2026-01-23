package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;
    @Autowired
    private com.project.back_end.services.Service service;

    @GetMapping("/{token}")
    public Object getPatient(@PathVariable String token) {
        var resp = service.validateToken(token, "patient");
        if (resp != null) {
            return resp;
        } else {
            return patientService.getPatientDetails(token);
        }
    }

    @PostMapping()
    public Object createPatient(@RequestBody Patient patient) {

            return patientService.createPatient(patient);

    }

    @PostMapping("/login")
    public Object login(@RequestBody Login login) {
        return service.validatePatientLogin(login);
    }

    @GetMapping("/{id}/{user}/{token}")
    public Map<String,Object> getPatientAppointment(@PathVariable Long id, @PathVariable String user,
                                        @PathVariable String token) {
        var resp = service.validateToken(token, "patient");
        if (resp != null) {
            return Map.of("message", resp.getBody().get("message"));
        } else {
            return Map.of("appointments", patientService.getPatientAppointment(id));
        }
    }

    @GetMapping("/filter/{condition}/{name}/{token}")
    public  Map<String,Object> filterPatientAppointment(@PathVariable String condition,
                                           @PathVariable String name,
                                           @PathVariable String token) {
        var resp = service.validateToken(token, "patient");
        if (resp != null) {
            return Map.of("message", resp.getBody().get("message"));
        } else {
            return Map.of("appointments", service.filterPatient(condition, name, token));
        }
    }


}


