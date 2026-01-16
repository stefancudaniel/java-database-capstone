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

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST controller that serves JSON responses.
//    - Use `@RequestMapping("${api.path}doctor")` to prefix all endpoints with a configurable API path followed by "doctor".
//    - This class manages doctor-related functionalities such as registration, login, updates, and availability.


// 2. Autowire Dependencies:
//    - Inject `DoctorService` for handling the core logic related to doctors (e.g., CRUD operations, authentication).
//    - Inject the shared `Service` class for general-purpose features like token validation and filtering.
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private Service service;



// 3. Define the `getDoctorAvailability` Method:
//    - Handles HTTP GET requests to check a specific doctor’s availability on a given date.
//    - Requires `user` type, `doctorId`, `date`, and `token` as path variables.
//    - First validates the token against the user type.
//    - If the token is invalid, returns an error response; otherwise, returns the availability status for the doctor.

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


// 4. Define the `getDoctor` Method:
//    - Handles HTTP GET requests to retrieve a list of all doctors.
//    - Returns the list within a response map under the key `"doctors"` with HTTP 200 OK status.


    @GetMapping("/get")
    public Map<String, Object> getDoctor() {
        return Map.of("doctors", doctorService.getDoctors());
    }


// 5. Define the `saveDoctor` Method:
//    - Handles HTTP POST requests to register a new doctor.
//    - Accepts a validated `Doctor` object in the request body and a token for authorization.
//    - Validates the token for the `"admin"` role before proceeding.
//    - If the doctor already exists, returns a conflict response; otherwise, adds the doctor and returns a success message.

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


// 6. Define the `doctorLogin` Method:
//    - Handles HTTP POST requests for doctor login.
//    - Accepts a validated `Login` DTO containing credentials.
//    - Delegates authentication to the `DoctorService` and returns login status and token information.
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }


// 7. Define the `updateDoctor` Method:
//    - Handles HTTP PUT requests to update an existing doctor's information.
//    - Accepts a validated `Doctor` object and a token for authorization.
//    - Token must belong to an `"admin"`.
//    - If the doctor exists, updates the record and returns success; otherwise, returns not found or error messages.

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


// 8. Define the `deleteDoctor` Method:
//    - Handles HTTP DELETE requests to remove a doctor by ID.
//    - Requires both doctor ID and an admin token as path variables.
//    - If the doctor exists, deletes the record and returns a success message; otherwise, responds with a not found or error message.


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


// 9. Define the `filter` Method:
//    - Handles HTTP GET requests to filter doctors based on name, time, and specialty.
//    - Accepts `name`, `time`, and `speciality` as path variables.
//    - Calls the shared `Service` to perform filtering logic and returns matching doctors in the response.

    @GetMapping("/filter/{name}/{time}/{speciality}")
    public Map<String, Object> filterDoctors(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {
        return service.filterDoctor(name, speciality, time);//Map.of("doctors", doctors);
    }

}
