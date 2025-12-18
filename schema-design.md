## MySQL Database Design

### Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor: INT, Foreign Key → doctors(id)
- patient: INT, Foreign Key → patients(id)
- appointmentTime: DATETIME, Not Null
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)

### Table: patients
- id: INT, Primary Key, Auto Increment
- name: VARCHAR(100), NOT NULL
- email: VARCHAR(150), UNIQUE, NULL
- password: VARCHAR(150), UNIQUE, NOT NULL
- phone: VARCHAR(20), UNIQUE, NOT NULL
- address: TEXT, NULL
- is_active: BOOLEAN, NOT NULL, DEFAULT FALSE 

Comments / Design Decisions:
- Email and phone uniqueness enforced at DB level.
- Email/phone format validation should be handled in application code, not DB.
- Patients should NOT be hard-deleted → historical appointments must remain.


### Table: doctors
- id: INT, Primary Key, Auto Increment
- name: VARCHAR(100), NOT NULL
- specialty: VARCHAR(150), NOT NULL
- email: VARCHAR(150), UNIQUE, NOT NULL
- password: VARCHAR(150), UNIQUE, NOT NULL
- phone: VARCHAR(20), UNIQUE, NOT NULL
- clinic_location_id: INT, Foreign Key → clinic_locations(id)
- is_active: BOOLEAN, DEFAULT TRUE

Comments / Design Decisions:
- Doctors are linked to a clinic location.
- Deactivating doctors instead of deleting avoids breaking appointment history.


### Table: admin
- id: INT, Primary Key, Auto Increment
- username: VARCHAR(100), UNIQUE, NOT NULL
- password: VARCHAR(255), NOT NULL

### Table: clinic_locations
- id: INT, Primary Key, Auto Increment
- name: VARCHAR(150), NOT NULL
- address: TEXT, NOT NULL
- phone: VARCHAR(20), NOT NULL
- created_at: DATETIME, NOT NULL, DEFAULT CURRENT_TIMESTAMP

Comments:
- Allows multi-branch clinics.
- Doctors are assigned to locations.


### Table: payments
- id: INT, Primary Key, Auto Increment
- appointment_id: INT, NOT NULL, Foreign Key → appointments(id)
- amount: DECIMAL(10,2), NOT NULL
- payment_method: ENUM('Cash','Card','Online'), NOT NULL
- payment_status: ENUM('Pending','Paid','Refunded'), NOT NULL
- paid_at: DATETIME, NULL

Comments:
- Payment is tied to an appointment.
- Allows partial or delayed payments.


### Table: doctor_availability
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, NOT NULL, Foreign Key → doctors(id)
- day_of_week: ENUM('Mon','Tue','Wed','Thu','Fri','Sat','Sun'), NOT NULL
- start_time: TIME, NOT NULL
- end_time: TIME, NOT NULL

Design Answer:
- Yes, doctors should have defined availability
- Appointment booking logic checks availability + overlapping appointments.


## MongoDB Collection Design

### Collection: prescriptions

```json
{
  "_id": "ObjectId('64abc123456')",
  "patientName": "John Smith",
  "appointmentId": 51,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours."
}
```

### Collection: pacient_feedback
```json
{
  "_id": "ObjectId('65fa91c8b32d9e0012a4bc10')",

  "patient": {
    "patient_id": 128,
    "display_name": "Sarah M.",
    "age_group": "30-39"
  },

  "appointment_id": 742,
  "doctor_id": 21,
  "clinic_location_id": 3,

  "rating": {
    "overall": 4.5,
    "categories": {
      "wait_time": 4,
      "doctor_communication": 5,
      "clinic_cleanliness": 4
    }
  },

  "comments": "Doctor was very attentive, but the waiting time was longer than expected.",

  "tags": ["long_wait", "friendly_doctor", "clean_facility"],

  "sentiment": {
    "score": 0.82,
    "label": "positive"
  },

  "metadata": {
    "submitted_via": "mobile_app",
    "language": "en",
    "ip_hash": "a9f3c1d9e"
  },

  "follow_up": {
    "requested": true,
    "preferred_contact": "email"
  },

  "created_at": "2025-03-12T14:22:10Z",
  "schema_version": 1
}
```

