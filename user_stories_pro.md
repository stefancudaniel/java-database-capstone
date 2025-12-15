# User Story Template
**Title:**
_As a [user role], I want [feature/goal], so that [reason]._
**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]
**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]

**Admin access:**
_As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely._
**Acceptance Criteria:**
1. There must be an admin role on the platform
2. All users must have a username and password
3. The username and password provides the access on the platform
**Priority:** [Medium]
**Story Points:** [5]
**Notes:**
- [Passwords must be securely stored using encryption.]

**Admin logout:**
_As an admin, I want to logout from the platform, so that my account and system are safe._
**Acceptance Criteria:**
1. When an admin user is logged, a button with "Log Out" appears.
2. Clicking the button will delog the admin user .

**Priority:** [Medium]
**Story Points:** [2]
**Notes:**
- [This story can be the same for all users.]
- [Logout should invalidate authentication tokens or sessions.]


**Admin add doctor users:**
_As an admin user, I want to be able to add doctors to the platform, so that the doctor user can be created._
**Acceptance Criteria:**
1. On the admin page there must be a button "Create doctor", that opens a page where a doctor user is created.
2. On the admin page there is a list with all the doctors.
3. [Criteria 3]
**Priority:** [Medium]
**Story Points:** [5]
**Notes:**
The information needed for creating a doctor user still need to be clarified.

**Admin delete doctor users:**
_As a an admin, I want to be able to delete doctor users, so that in case a doctor is no longer needed at the clinic he will be deleted._
**Acceptance Criteria:**
1. On the admin page there must be a button "Delete doctor" on every listed doctor in the list.
2. The "Delete doctor" button will delete the doctor use from the database.
3. After a doctor is deleted, the doctor user list will be refreshed and that specific doctor will no longer be present in the list.
**Priority:** [Medium]
**Story Points:** [3]
**Notes:**
Please take in consideration any relations between the doctor and other element from the database.

**Get appointments:**
_As an admin user, I want to get all the appointments in a month and generate statisctics, so that a clear view over the appointments is available._
**Acceptance Criteria:**
1. A button "Reports" is available on the admin page.
2. Clicking the button will display the number of appointments in that month.
**Priority:** [Low]
**Story Points:** [5]
**Notes:**

**View Doctors Without Login:**
_As a patient, I want to view a list of doctors without logging in, so that I can explore available options before registering._
**Acceptance Criteria:**
1. The system allows unauthenticated users to access the doctor list.
2. The doctor list displays basic information such as name and specialization.
3. Booking actions are disabled until the user logs in or registers.
**Priority:** [Medium]
**Story Points:** [3]
**Notes:**
- [Sensitive information (availability, contact details) should not be shown to unauthenticated users.]

**Patient Registration:**
_As a patient, I want to sign up using my email and password, so that I can book appointments._
**Acceptance Criteria:**
1. The system allows registration using a valid email and password.
2. Duplicate email registrations are prevented.
3. A successful registration grants access to patient features.
**Priority:** [High]
**Story Points:** [5]
**Notes:**
- [Passwords must be securely stored using encryption.]

**Patient Login:**
_As a patient, I want to log into the portal, so that I can manage my bookings._
**Acceptance Criteria:**
1.The system authenticates users using valid credentials.
2. Invalid credentials display an error message.
3. Successful login redirects the user to the patient dashboard.
**Priority:** [High]
**Story Points:** [3]
**Notes:**
- [Session management should follow security best practices.]

**Patient Logout:**
_As a patient, I want to log out of the portal, so that I can secure my account._
**Acceptance Criteria:**
1. The system terminates the user session on logout.
2. The user is redirected to the login or home page.
3. Protected pages are inaccessible after logout.
**Priority:** [Medium]
**Story Points:** [2]
**Notes:**
- [Logout should invalidate authentication tokens or sessions.]

**Book Appointment:**
_As a patient, I want to book a one-hour appointment with a doctor, so that I can consult with a healthcare professional._
**Acceptance Criteria:**
1.The system allows booking only available one-hour time slots.
2. The selected doctor must be available during the chosen time.
3. A confirmation message is displayed after successful booking.
**Priority:** [High]
**Story Points:** [5]
**Notes:**
- [Double booking must be prevented.]

**View Upcoming Appointments:**
As a patient, I want to view my upcoming appointments, so that I can prepare accordingly._
**Acceptance Criteria:**
1. The system displays all future appointments for the logged-in patient.
2. Appointment details include date, time, and doctor name.
3. Past appointments are not shown in the upcoming list.
**Priority:** [Medium]
**Story Points:** [3]
**Notes:**
- [Appointments should be sorted by date and time.]

**Doctor Login:**
_As a doctor, I want to log into the portal, so that I can manage my appointments._
**Acceptance Criteria:**
1. The system allows doctors to log in using valid credentials.
2. Invalid login attempts display an appropriate error message.
3. Successful login redirects the doctor to their dashboard.
**Priority:** [High]
**Story Points:** [3]
**Notes:**
- [Authentication must follow security best practices.]

**Doctor Logout:**
_As a doctor, I want to log out of the portal, so that I can protect my data._
**Acceptance Criteria:**
1. The system terminates the doctor’s active session upon logout.
2. The doctor is redirected to the login or home page.
3. Restricted pages cannot be accessed after logout.
**Priority:** [Medium]
**Story Points:** [2]
**Notes:**
- [Logout should invalidate sessions or authentication tokens.]

**View Appointment Calendar:**
_As a doctor, I want to view my appointment calendar, so that I can stay organized._
**Acceptance Criteria:**
1. The system displays only the logged-in doctor’s appointments.
2. Appointments are shown with date, time, and patient name.
3. The calendar is ordered chronologically.
**Priority:** [High]
**Story Points:** [4]
**Notes:**
- [Calendar views may include daily and weekly options.]

**Manage Availability:**
_As a doctor, I want to mark my unavailability, so that patients can only book available time slots._
**Acceptance Criteria:**
1. The doctor can mark time slots as unavailable.
2. Unavailable slots cannot be booked by patients.
3. Availability updates are saved immediately.
**Priority:** [High]
**Story Points:** [5]
**Notes:**
- [Existing appointments should not be affected by availability changes.]

**Update Doctor Profile:**
_As a doctor, I want to update my profile with specialization and contact information, so that patients have up-to-date information._
**Acceptance Criteria:**
1.The doctor can update specialization and contact details.
2. Updated information is saved and displayed correctly.
3. Invalid input displays validation errors.
**Priority:** [Medium]
**Story Points:** [3]
**Notes:**
- [Profile updates should be visible to patients viewing doctor details.]

**View Patient Details:**
_As a doctor, I want to view patient details for upcoming appointments, so that I can be prepared._
**Acceptance Criteria:**
1. The system displays patient details only for scheduled appointments.
2. Patient information is accessible only to the assigned doctor.
3. Data is displayed securely and read-only.
**Priority:** [High]
**Story Points:** [4]
**Notes:**
- [Access must comply with privacy and data protection policies.]


