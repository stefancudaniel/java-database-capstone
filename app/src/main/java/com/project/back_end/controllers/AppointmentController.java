package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}" + "appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private Service service;

    //TODO: complete the else block
    @GetMapping("/{date}/{patientName}/{token}")
    public Object getAppointments(@PathVariable String date,
                                  @PathVariable String patientName,
                                  @PathVariable String token) {

        ResponseEntity<Map<String, String>> resp = service.validateToken(token, "doctor");
        if (resp != null) {
            return resp;
        }
        return appointmentService.getAppointments(token, date, patientName);
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> bookAppointment(@RequestBody Appointment appointment,
                                                               @PathVariable String token) {
        ResponseEntity<Map<String, String>> resp = service.validateToken(token, "patient");

        switch (service.validateAppointment(appointment))
        {
            case 1:
                if(appointmentService.bookAppointment(appointment) == 1) {;
                    return ResponseEntity.ok(Map.of("message", "Appointment booked successfully."));
                } else {
                    return ResponseEntity.ok(Map.of("message", "Failed to book appointment due to internal error."));
                }
            case 0:
                return ResponseEntity.badRequest().body(Map.of("message", "No matching time slot is found"));
            case -1:
                return ResponseEntity.badRequest().body(Map.of("message", "Doctor does not exist"));
            default:
                return ResponseEntity.status(500).body(Map.of("message", "Internal server error"));
        }
    }

    @PutMapping("/{token}")
    public Map<String, String> updateAppointment(@RequestBody Appointment appointment,
                                                                 @PathVariable String token) {
        var resp = service.validateToken(token, "patient");
        if (resp != null) {
            return resp.getBody();
        }
        return appointmentService.updateAppointment(appointment);
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(@PathVariable Long appointmentId,
                                                                 @PathVariable String token) {
        ResponseEntity<Map<String, String>> resp = service.validateToken(token, "patient");
        if (resp != null) {
            return resp;
        }
        return appointmentService.cancelAppointment(appointmentId, token);
    }

}
