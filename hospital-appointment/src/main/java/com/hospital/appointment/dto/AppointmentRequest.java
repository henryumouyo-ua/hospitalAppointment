package com.hospital.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequest {

    @NotNull(message = "patientId is required")
    private Long patientId;

    @NotNull(message = "doctorId is required")
    private Long doctorId;

    @NotNull(message = "appointmentDateTime is required")
    @Future(message = "appointmentDateTime must be in the future")
    private LocalDateTime appointmentDateTime;

    private String reasonForVisit;
}
