// appointmentRow.js
export function getAppointments(appointment) {
  const tr = document.createElement("tr");

  tr.innerHTML = `
      <td class="patient-id">${appointment.patientName}</td>
      <td>${appointment.doctorName}</td>
      <td>${appointment.date}</td>
      <td>${appointment.time}</td>
      <td><img src="../assets/images/edit/edit.png" alt="action" class="prescription-btn" width="24" height="24" style="width:24px;height:24px;object-fit:contain;" data-id="${appointment.id}"></img></td>
    `;

  // Attach event listeners
  tr.querySelector(".prescription-btn").addEventListener("click", () => {
    window.location.href = `addPrescription.html?id=${patient.id}`;
  });

  return tr;
}
