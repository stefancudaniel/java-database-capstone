// patientDashboard.js
import { getDoctors } from './services/doctorServices.js';
import { openModal } from './components/modals.js';
import { createDoctorCard } from './components/doctorCard.js';
import { filterDoctors } from './services/doctorServices.js';//call the same function to avoid duplication coz the functionality was same
import { patientSignup, patientLogin } from './services/patientServices.js';



document.addEventListener("DOMContentLoaded", () => {
  loadDoctorCards();
});

document.addEventListener("DOMContentLoaded", () => {
  const btn = document.getElementById("patientSignup");
  if (btn) {
    btn.addEventListener("click", () => openModal("patientSignup"));
  }
});

document.addEventListener("DOMContentLoaded", () => {
  const loginBtn = document.getElementById("patientLogin")
  if (loginBtn) {
    loginBtn.addEventListener("click", () => {
      openModal("patientLogin")
    })
  }
})

function loadDoctorCards() {
  getDoctors()
    .then(doctors => {
      const contentDiv = document.getElementById("content");
      contentDiv.innerHTML = "";

      doctors.forEach(doctor => {
        const card = createDoctorCard(doctor);
        contentDiv.appendChild(card);
      });
    })
    .catch(error => {
      console.error("Failed to load doctors:", error);
    });
}
// Filter Input
document.getElementById("searchBarForDoctors").addEventListener("input", filterDoctorsOnChange);
document.getElementById("filterTimeForDoctors").addEventListener("change", filterDoctorsOnChange);
document.getElementById("filterSpecialtyForDoctors").addEventListener("change", filterDoctorsOnChange);



async function filterDoctorsOnChange() {
  const searchBar = document.getElementById("searchBarForDoctors").value.trim();
  const filterTime = document.getElementById("filterTimeForDoctors").value;
  const filterSpecialty = document.getElementById("filterSpecialtyForDoctors").value;


  const name = searchBar.length > 0 ? searchBar : null;
  const time = filterTime.length > 0 ? filterTime : null;
  const specialty = filterSpecialty.length > 0 ? filterSpecialty : null;

  try {
      const filteredDoctors = await filterDoctors(name, time, specialty);
      if (filteredDoctors.length > 0) {
        renderDoctorCards(filteredDoctors);
      } else {
        const contentDiv = document.getElementById('content');
        contentDiv.innerHTML = '<p>No doctors found with the given filters.</p>';
      }
      }
      catch (error) {
      alert(`Error filtering doctors: ${error.message}`);
      }
  /*try{
    const filteredDoctors = await filterDoctors(name, time, specialty);
    const contentDiv = document.getElementById("content");
    contentDiv.innerHTML = "";

    if (filterDoctors.length > 0) {
        console.log(doctors);
        doctors.forEach(doctor => {
            const card = createDoctorCard(doctor);
            contentDiv.appendChild(card);
        });
    } else {
    contentDiv.innerHTML = "<p>No doctors found with the given filters.</p>";
    console.log("Nothing");
  }
    }
    catch(error ) {
      console.error("Failed to filter doctors:", error);
      alert("❌ An error occurred while filtering doctors.");
    };*/
}

window.signupPatient = async function () {
  try {
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const phone = document.getElementById("phone").value;
    const address = document.getElementById("address").value;

    const data = { name, email, password, phone, address };
    const { success, message } = await patientSignup(data);
    if (success) {
      alert(message);
      document.getElementById("modal").style.display = "none";
      window.location.reload();
    }
    else alert(message);
  } catch (error) {
    console.error("Signup failed:", error);
    alert("❌ An error occurred while signing up.");
  }
};

window.loginPatient = async function () {
  try {
    const email = document.getElementById("patientEmail").value;
    const password = document.getElementById("patientPassword").value;

    const data = {
      email,
      password
    }
    console.log("loginPatient :: ", data)
    const response = await patientLogin(data);
    console.log("Status Code:", response.status);
    console.log("Response OK:", response.ok);
    if (response.ok) {
      const result = await response.json();
      console.log(result);
      selectRole('loggedPatient');
      localStorage.setItem('token', result.token)
      window.location.href = '/pages/loggedPatientDashboard.html';
    } else {
      alert('❌ Invalid credentials!');
    }
  }
  catch (error) {
    alert("❌ Failed to Login : ", error);
    console.log("Error :: loginPatient :: ", error)
  }


}

function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById('content');
  contentDiv.innerHTML = '';
  doctors.forEach(doctor => {
    const doctorCard = createDoctorCard(doctor);
    contentDiv.appendChild(doctorCard);
  });
}
