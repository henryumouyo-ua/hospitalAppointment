package com.hospital.appointment.service.impl;

import com.hospital.appointment.exception.ResourceNotFoundException;
import com.hospital.appointment.model.Patient;
import com.hospital.appointment.repository.PatientRepository;
import com.hospital.appointment.service.PatientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findByActiveTrue();
    }

    @Override
    public void deletePatient(Long id) {
        // Soft delete: appointment history referencing this patient must
        // remain intact, so we flip the active flag rather than removing
        // the row (see the data model doc, section 5.2).
        Patient patient = getPatientById(id);
        patient.setActive(false);
        patientRepository.save(patient);
    }
}
