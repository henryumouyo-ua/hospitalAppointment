# Hospital Appointment API

## 1. Set up PostgreSQL

Unlike MySQL, PostgreSQL won't create the database for you automatically —
create it once before running the app:

CREATE DATABASE hospital_appointments;


## 2. Configure the connection

Edit `src/main/resources/application.properties`:

spring.datasource.url=jdbc:postgresql://localhost:5432/hospital_appointments
spring.datasource.username=postgres
spring.datasource.password=your_postgres_password

Replace the username/password with your own PostgreSQL credentials.

## 3. Run the app

From the project root: Note that port 8080 may already be in use, so use a different port like 8089 to run. 

