package com.hospital.appointment.service.impl;

import com.hospital.appointment.dto.AppointmentRequest;
import com.hospital.appointment.dto.AppointmentResponse;
import com.hospital.appointment.dto.RescheduleRequest;
import com.hospital.appointment.exception.InvalidAppointmentException;
import com.hospital.appointment.exception.ResourceNotFoundException;
import com.hospital.appointment.model.Appointment;
import com.hospital.appointment.model.AppointmentStatus;
import com.hospital.appointment.model.Doctor;
import com.hospital.appointment.model.Patient;
import com.hospital.appointment.repository.AppointmentRepository;
import com.hospital.appointment.repository.DoctorRepository;
import com.hospital.appointment.repository.PatientRepository;
import com.hospital.appointment.service.AppointmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                   PatientRepository patientRepository,
                                   DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with id: " + request.getPatientId()));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with id: " + request.getDoctorId()));

        if (!doctor.isActive()) {
            throw new InvalidAppointmentException(
                    "Doctor with id " + doctor.getId() + " is no longer active");
        }

        boolean slotTaken = appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatusNot(
                doctor.getId(), request.getAppointmentDateTime(), AppointmentStatus.CANCELLED);

        if (slotTaken) {
            throw new InvalidAppointmentException(
                    "Doctor is already booked at " + request.getAppointmentDateTime());
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(request.getAppointmentDateTime());
        appointment.setReasonForVisit(request.getReasonForVisit());
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment saved = appointmentRepository.save(appointment);
        return AppointmentResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public AppointmentResponse cancelAppointment(Long appointmentId) {
        Appointment appointment = findAppointmentOrThrow(appointmentId);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new InvalidAppointmentException("Appointment is already cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment saved = appointmentRepository.save(appointment);
        return AppointmentResponse.fromEntity(saved);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public AppointmentResponse rescheduleAppointment(Long appointmentId, RescheduleRequest request) {
        Appointment appointment = findAppointmentOrThrow(appointmentId);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new InvalidAppointmentException("Cannot reschedule a cancelled appointment");
        }

        boolean slotTaken = appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatusNot(
                appointment.getDoctor().getId(), request.getNewAppointmentDateTime(), AppointmentStatus.CANCELLED);

        if (slotTaken) {
            throw new InvalidAppointmentException(
                    "Doctor is already booked at " + request.getNewAppointmentDateTime());
        }

        appointment.setAppointmentDateTime(request.getNewAppointmentDateTime());
        appointment.setStatus(AppointmentStatus.RESCHEDULED);

        Appointment saved = appointmentRepository.save(appointment);
        return AppointmentResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public AppointmentResponse completeAppointment(Long appointmentId) {
        Appointment appointment = findAppointmentOrThrow(appointmentId);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new InvalidAppointmentException("Cannot complete a cancelled appointment");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new InvalidAppointmentException("Appointment is already marked completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        Appointment saved = appointmentRepository.save(appointment);
        return AppointmentResponse.fromEntity(saved);
    }

    @Override
    public AppointmentResponse getAppointmentById(Long appointmentId) {
        return AppointmentResponse.fromEntity(findAppointmentOrThrow(appointmentId));
    }

    @Override
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(AppointmentResponse::fromEntity)
                .toList();
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(AppointmentResponse::fromEntity)
                .toList();
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(AppointmentResponse::fromEntity)
                .toList();
    }

    private Appointment findAppointmentOrThrow(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + appointmentId));
    }
}
