package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

     @Autowired
     private PrescriptionService prescriptionService;
     @Autowired
     private Service service;
     @Autowired
     private AppointmentService appointmentService;

    @PostMapping("/{token}")
    public Object savePrescription(@RequestBody Prescription prescription,
                                   @PathVariable String token) {
        var resp = service.validateToken(token, "doctor");
        if (resp != null) {
            return resp;
        } else {

            return prescriptionService.savePrescription(prescription);
        }
    }

    @GetMapping("/{appointmentId}/{token}")
    public Object getPrescription(@PathVariable Long appointmentId,
                                  @PathVariable String token) {
        var resp = service.validateToken(token, "doctor");
        if (resp != null) {
            return resp;
        } else {
            return prescriptionService.getPrescription(appointmentId);
        }
    }

}
