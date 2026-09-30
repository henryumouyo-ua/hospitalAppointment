package com.hospital.appointment.service;

import com.hospital.appointment.model.Patient;

import java.util.List;

public interface PatientService {
    Patient createPatient(Patient patient);
    Patient getPatientById(Long id);
    List<Patient> getAllPatients();
    void deletePatient(Long id);
}
