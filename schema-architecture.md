The Smart Clinic application is built using a clean, layered Spring Boot architecture that clearly separates responsibilities across the system. 
Users interact with the platform through either Thymeleaf-based web dashboards or REST API clients, depending on the use case. 
Incoming requests are handled by controllers, which act as entry points and delegate processing to the service layer. 
The service layer contains the core business logic, validations, and workflow coordination, while repositories abstract all database interactions. 
Structured data such as users and appointments is stored in MySQL, while flexible document-based data like prescriptions is managed in MongoDB. 
Data retrieved from these databases is mapped into Java entities and document models, which are then returned to clients as either rendered HTML views or JSON responses, 
completing the request–response cycle.

1.The user accesses the system through either Thymeleaf-based dashboards (AdminDashboard or DoctorDashboard) or REST-based modules such as Appointments, PatientDashboard, and PatientRecord.

2.User actions are routed to the appropriate controller within the Spring Boot application: Thymeleaf controllers handle server-rendered web pages, while REST controllers handle JSON-based API requests.

3.The controllers delegate all processing to the service layer, which contains the core business logic, validations, and workflow coordination.

4.The service layer interacts with repositories to retrieve or persist data, using MySQL repositories for relational data and a MongoDB repository for document-based data.

5.MySQL repositories access the MySQL database for structured entities such as patients, doctors, appointments, and admins, while the MongoDB repository accesses the MongoDB database for prescription data.

6.Data retrieved from the databases is mapped into application models: JPA entities for MySQL data and document models for MongoDB data.

7.The populated models are returned to the controller, which either renders them into Thymeleaf HTML views or serializes them into JSON responses, completing the request–response cycle.
