/*
  Import getAllAppointments to fetch appointments from the backend
  Import createPatientRow to generate a table row for each patient appointment
*/

// javascript
import { getAllAppointments } from "/js/services/appointmentRecordService.js";
import { createPatientRow } from "/js/components/patientRows.js";
//import { renderContent } from "/js/render.js";

let tableBody = null;
let selectedDate = new Date().toISOString().split('T')[0]; // Default to today
const token = localStorage.getItem('token');
let patientName = null; // For filtering by patient name

async function loadAppointments() {
  try {
    // Ensure we have a tbody reference (try common IDs/selectors)
    if (!tableBody) {
      tableBody = document.getElementById('patientTableBody');
    }

    if (!tableBody) {
      console.error('Table body element not found');
      return;
    }
    const appointments = await getAllAppointments(selectedDate, patientName, token);
    // Clear existing rows
   // tableBody.innerHTML = '';
    tableBody.innerHTML = "";

    const actionTh = document.querySelector("#patientTable thead tr th:last-child");
      if (actionTh) {
        actionTh.style.display = "table-cell"; // Always show "Actions" column
      }

      if (!appointments.length) {
        tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center;">No Appointments Found</td></tr>`;
        return;
      }

      /*appointments.forEach(appointment => {
      createPatientRow(appointment.patient, appointment.id, appointment.doctorId).then((row) => {
        tableBody.appendChild(row);
      })*/

      for (const appointment of appointments) {
        try {
          const row = await createPatientRow(appointment.patient, appointment.id, appointment.doctorId);
          if (row) tableBody.appendChild(row);
          else console.warn('createPatientRow returned no element for', appointment);
        } catch (err) {
          console.error('Error creating row for appointment', appointment, err);
        }
      }
       /* const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${appointment.patientId }</td>
          <td>${appointment.patientName}</td>
          <td>${appointment.patientPhone}</td>
          <td>${appointment.patientEmail}</td>
          <td>${appointment.status == 0 ? `<img src="../assets/images/edit/edit.png" alt="Edit" width="24" height="24" style="width:24px;height:24px;object-fit:contain;" class="prescription-btn" data-id="${appointment.patientId}">` : "-"}</td>
        `;

        if (appointment.status == 0) {
          const actionBtn = tr.querySelector(".prescription-btn");
          actionBtn?.addEventListener("click", () => redirectToUpdatePage(appointment));
        }

        tableBody.appendChild(tr);*/
     // });
      } catch (error) {
    console.error("Error loading appointments:", error);
    if (!tableBody) return;
    tableBody.innerHTML = '';
    const errorRow = document.createElement('tr');
    const errorCell = document.createElement('td');
    errorCell.colSpan = 5;
    errorCell.textContent = 'Error loading appointments. Try again later.';
    errorRow.appendChild(errorCell);
    tableBody.appendChild(errorRow);
  }

    /*let list ;
        if (appointments && typeof appointments === 'object' && appointments.appointments && Array.isArray(appointments.appointments)) {
          // If the payload is an object with an 'appointments' array property, use that array
          list = appointments.appointments;
        } else if (Array.isArray(appointments)) {
          // If the payload is already an array, use it directly
          list = appointments;
        } else if (typeof appointments === 'object' && appointments !== null && (appointments.id || appointments.patientId)) {
          // If it's a single appointment object, wrap it in an array
          list = [appointments];
        } else {
          // Handle unexpected payload structure
          console.warn('Unexpected appointments payload:', appointments);
          const noDataRow = document.createElement('tr');
          const noDataCell = document.createElement('td');
          noDataCell.colSpan = 5;
          noDataCell.textContent = 'No Appointments found for today.';
          noDataRow.appendChild(noDataCell);
          tableBody.appendChild(noDataRow);
          return;
        }



    // Render rows
    list.forEach(appointment => {
      const patient = {
        id: appointment.patientId,
        name: appointment.patientName,
        phone: appointment.patientPhone,
        email: appointment.patientEmail
      };
      const row = createPatientRow(appointment.id, patient, appointment.date, appointment.time, appointment.status);
      if (row) tableBody.appendChild(row);
    });
  } catch (error) {
    console.error('Error loading appointments:', error);
    if (!tableBody) return;
    tableBody.innerHTML = '';
    const errorRow = document.createElement('tr');
    const errorCell = document.createElement('td');
    errorCell.colSpan = 5;
    errorCell.textContent = 'Error loading appointments. Try again later.';
    errorRow.appendChild(errorCell);
    tableBody.appendChild(errorRow);
  }*/
}

// Attach DOM listeners after DOM is ready
window.addEventListener('DOMContentLoaded', () => {
  //tableBody = document.getElementById('appointmentsTableBody');
 tableBody = document.getElementById('patientTableBody');
  const searchBar = document.getElementById('searchBar');
  if (searchBar) {
    searchBar.addEventListener('input', (event) => {
      const input = event.target.value.trim();
      patientName = input !== '' ? input : null;
      loadAppointments();
    });
  }

  const todayBtn = document.getElementById('todayBtn');
  if (todayBtn) {
    todayBtn.addEventListener('click', () => {
      selectedDate = new Date().toISOString().split('T')[0];
      const datePicker = document.getElementById('datePicker');
      if (datePicker) datePicker.value = selectedDate;
      loadAppointments();
    });
  }

  const datePicker = document.getElementById('datePicker');
  if (datePicker) {
    if (!datePicker.value) datePicker.value = selectedDate;
    datePicker.addEventListener('change', (event) => {
      selectedDate = event.target.value;
      loadAppointments();
    });
  }

  if (typeof renderContent === 'function') {
    renderContent(); // only call if available
  }

  loadAppointments();
});

/*import { getAllAppointments } from "/js/services/appointmentRecordService.js";
import { createPatientRow } from "/js/components/patientRows.js";
//import { renderContent } from "/js/render.js";


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
 const tableBody = document.getElementById('appointmentsTableBody');
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
*/



//import { getAllAppointments } from "/js/services/appointmentRecordService.js";
//import { createPatientRow } from "/js/components/patientRows.js";



  //Get the table body where patient rows will be added
   // let tableBody = document.querySelector('#appointmentsTable tbody');
  /*Initialize selectedDate with today's date in 'YYYY-MM-DD' format
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