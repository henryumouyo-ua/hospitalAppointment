package com.hospital.appointment.service.impl;

import com.hospital.appointment.exception.ResourceNotFoundException;
import com.hospital.appointment.model.Doctor;
import com.hospital.appointment.repository.DoctorRepository;
import com.hospital.appointment.service.DoctorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findByActiveTrue();
    }

    @Override
    public void deleteDoctor(Long id) {
        // Soft delete: appointment history referencing this doctor must
        // remain intact, so we flip the active flag rather than removing
        // the row (see the data model doc, section 5.2).
        Doctor doctor = getDoctorById(id);
        doctor.setActive(false);
        doctorRepository.save(doctor);
    }
}
