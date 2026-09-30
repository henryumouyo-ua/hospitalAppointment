package com.hospital.appointment.repository;

import com.hospital.appointment.model.Appointment;
import com.hospital.appointment.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByStatus(AppointmentStatus status);

    // Used to check for double-booking a doctor at the same time slot
    boolean existsByDoctorIdAndAppointmentDateTimeAndStatusNot(
            Long doctorId,
            LocalDateTime appointmentDateTime,
            AppointmentStatus status
    );
}
