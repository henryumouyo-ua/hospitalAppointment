package com.hospital.appointment.service;

import com.hospital.appointment.dto.AppointmentRequest;
import com.hospital.appointment.dto.AppointmentResponse;
import com.hospital.appointment.dto.RescheduleRequest;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse bookAppointment(AppointmentRequest request);

    AppointmentResponse cancelAppointment(Long appointmentId);

    AppointmentResponse rescheduleAppointment(Long appointmentId, RescheduleRequest request);

    AppointmentResponse completeAppointment(Long appointmentId);

    AppointmentResponse getAppointmentById(Long appointmentId);

    List<AppointmentResponse> getAllAppointments();

    List<AppointmentResponse> getAppointmentsByPatient(Long patientId);

    List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId);
}
