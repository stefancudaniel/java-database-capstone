/*
  Import getAllAppointments to fetch appointments from the backend
  Import createPatientRow to generate a table row for each patient appointment
*/

import { getAllAppointments } from "../services/appointmentServices.js";
import { createPatientRow } from "./patientRow.js";
import { renderContent } from "./dashboardLayout.js";

const tableBody = document.querySelector('#appointmentsTable tbody');
let selectedDate = new Date().toISOString().split('T')[0]; // Default to today
const token = localStorage.getItem('token');
let patientName = null; // For filtering by patient name

// Search bar event listener for filtering by patient name
document.getElementById('searchBar').addEventListener('input', (event) => {
  const input = event.target.value.trim();
  patientName = input !== '' ? input : null; // Set to null if empty
  loadAppointments();
});

// "Today" button click listener
document.getElementById('todayBtn').addEventListener('click', () => {
  selectedDate = new Date().toISOString().split('T')[0];
  document.getElementById('datePicker').value = selectedDate;
  loadAppointments();
});

// Date picker change listener
document.getElementById('datePicker').addEventListener('change', (event) => {
  selectedDate = event.target.value;
  loadAppointments();
});

// Function to load and display appointments
async function loadAppointments() {
  try {
    const appointments = await getAllAppointments(selectedDate, patientName, token);
    tableBody.innerHTML = ''; // Clear existing rows

    if (appointments.length === 0) {
      const noDataRow = document.createElement('tr');
      const noDataCell = document.createElement('td');
      noDataCell.colSpan = 5; // Assuming 5 columns in the table
      noDataCell.textContent = 'No Appointments found for today.';
      noDataRow.appendChild(noDataCell);
      tableBody.appendChild(noDataRow);
      return;
    }

    appointments.forEach(appointment => {
      const patient = {
        id: appointment.patientId,
        name: appointment.patientName,
        phone: appointment.patientPhone,
        email: appointment.patientEmail
      };
      const row = createPatientRow(appointment.id, patient, appointment.date, appointment.time, appointment.status);
      tableBody.appendChild(row);
    });
  } catch (error) {
    const errorRow = document.createElement('tr');
    const errorCell = document.createElement('td');
    errorCell.colSpan = 5;
    errorCell.textContent = 'Error loading appointments. Try again later.';
    errorRow.appendChild(errorCell);
    tableBody.appendChild(errorRow);
  }
}

// Initial load of appointments on page load
window.addEventListener('DOMContentLoaded', () => {
  renderContent(); // Assuming this sets up the UI layout
  loadAppointments();
});


/*

  Get the table body where patient rows will be added
  Initialize selectedDate with today's date in 'YYYY-MM-DD' format
  Get the saved token from localStorage (used for authenticated API calls)
  Initialize patientName to null (used for filtering by name)


  Add an 'input' event listener to the search bar
  On each keystroke:
    - Trim and check the input value
    - If not empty, use it as the patientName for filtering
    - Else, reset patientName to "null" (as expected by backend)
    - Reload the appointments list with the updated filter


  Add a click listener to the "Today" button
  When clicked:
    - Set selectedDate to today's date
    - Update the date picker UI to match
    - Reload the appointments for today


  Add a change event listener to the date picker
  When the date changes:
    - Update selectedDate with the new value
    - Reload the appointments for that specific date


  Function: loadAppointments
  Purpose: Fetch and display appointments based on selected date and optional patient name

  Step 1: Call getAllAppointments with selectedDate, patientName, and token
  Step 2: Clear the table body content before rendering new rows

  Step 3: If no appointments are returned:
    - Display a message row: "No Appointments found for today."

  Step 4: If appointments exist:
    - Loop through each appointment and construct a 'patient' object with id, name, phone, and email
    - Call createPatientRow to generate a table row for the appointment
    - Append each row to the table body

  Step 5: Catch and handle any errors during fetch:
    - Show a message row: "Error loading appointments. Try again later."


  When the page is fully loaded (DOMContentLoaded):
    - Call renderContent() (assumes it sets up the UI layout)
    - Call loadAppointments() to display today's appointments by default
*/
