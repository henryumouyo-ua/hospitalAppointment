package com.hospital.appointment.service;

import com.hospital.appointment.model.Doctor;

import java.util.List;

public interface DoctorService {
    Doctor createDoctor(Doctor doctor);
    Doctor getDoctorById(Long id);
    List<Doctor> getAllDoctors();
    void deleteDoctor(Long id);
}
