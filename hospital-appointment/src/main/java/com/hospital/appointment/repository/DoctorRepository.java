package com.hospital.appointment.repository;

import com.hospital.appointment.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findBySpecializationIgnoreCase(String specialization);
    List<Doctor> findByActiveTrue();
}
