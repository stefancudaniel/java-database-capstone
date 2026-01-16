/*
Import the overlay function for booking appointments from loggedPatient.js

  Import the deleteDoctor API function to remove doctors (admin role) from docotrServices.js

  Import function to fetch patient details (used during booking) from patientServices.js

  Function to create and return a DOM element for a single doctor card
    Create the main container for the doctor card
    Retrieve the current user role from localStorage
    Create a div to hold doctor information
    Create and set the doctor’s name
    Create and set the doctor's specialization
    Create and set the doctor's email
    Create and list available appointment times
    Append all info elements to the doctor info container
    Create a container for card action buttons
    === ADMIN ROLE ACTIONS ===
      Create a delete button
      Add click handler for delete button
     Get the admin token from localStorage
        Call API to delete the doctor
        Show result and remove card if successful
      Add delete button to actions container
   
    === PATIENT (NOT LOGGED-IN) ROLE ACTIONS ===
      Create a book now button
      Alert patient to log in before booking
      Add button to actions container
  
    === LOGGED-IN PATIENT ROLE ACTIONS === 
      Create a book now button
      Handle booking logic for logged-in patient   
        Redirect if token not available
        Fetch patient data with token
        Show booking overlay UI with doctor and patient info
      Add button to actions container
   
  Append doctor info and action buttons to the car
  Return the complete doctor card element
*/
//import { showBookingOverlay } from "../loggedPatient.js";
import { deleteDoctor } from "../services/doctorServices.js";
import { getPatientData } from "../services/patientServices.js";

export function createDoctorCard(doctor) {
  const card = document.createElement("div");
  card.className = "doctor-card";

  const infoContainer = document.createElement("div");
  infoContainer.className = "doctor-info";

  const nameElem = document.createElement("h3");
  nameElem.textContent = doctor.name;
    const specializationElem = document.createElement("p");
    specializationElem.textContent = `Specialization: ${doctor.specialty}`;
    const emailElem = document.createElement("p");
    emailElem.textContent = `Email: ${doctor.email}`;
    const availabilityElem = document.createElement("p");
    availabilityElem.textContent = `Available Times: ${doctor.availableTimes.join(", ")}`;

    infoContainer.appendChild(nameElem);
    infoContainer.appendChild(specializationElem);
    infoContainer.appendChild(emailElem);
    infoContainer.appendChild(availabilityElem);

    const actionsContainer = document.createElement("div");
    actionsContainer.className = "card-actions";

    const userRole = localStorage.getItem("userRole");

    // Admin Role Actions
    if (userRole === "admin") {
      const deleteButton = document.createElement("button");
      deleteButton.textContent = "Delete Doctor";
      deleteButton.className = "delete-button";
      deleteButton.onclick = async () => {
        const token = localStorage.getItem("token");
        const response = await deleteDoctor(doctor.id, token);
        if (response.success) {
          alert("Doctor deleted successfully.");
          card.remove();
        } else {
          alert(`Error deleting doctor: ${response.message}`);
        }
      };
      actionsContainer.appendChild(deleteButton);
    }

    // Patient (Not Logged-In) Role Actions
    else if (userRole !== "loggedPatient") {
      const bookButton = document.createElement("button");
      bookButton.textContent = "Book Now";
      bookButton.className = "book-button";
      bookButton.onclick = () => {
        alert("Please log in as a patient to book an appointment.");
      };
      actionsContainer.appendChild(bookButton);
    }
    // Logged-In Patient Role Actions
    else {
      const bookButton = document.createElement("button");
        bookButton.textContent = "Book Now";
        bookButton.className = "book-button";
        bookButton.onclick = async () => {
            const token = localStorage.getItem("token");
            if (!token) {
                alert("Session expired. Please log in again.");
                window.location.href = "/";
                return;
            }

        }
        actionsContainer.appendChild(bookButton);

        bookButton.onclick = async () => {
            const token = localStorage.getItem("token");
            if (!token) {
                alert("Session expired. Please log in again.");
                window.location.href = "/";
                return;
            }
            const patientResponse = await fetchPatientDetails(token);
            if (!patientResponse.success) {
                alert("Error fetching patient details. Please log in again.");
                window.location.href = "/";
                return;
            }
            const patient = patientResponse.data;
           // showBookingOverlay(doctor, patient);
        };
    }

    card.appendChild(infoContainer);
    card.appendChild(actionsContainer);

    return card;
}

