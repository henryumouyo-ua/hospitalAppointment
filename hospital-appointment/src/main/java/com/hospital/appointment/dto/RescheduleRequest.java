package com.hospital.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RescheduleRequest {

    @NotNull(message = "newAppointmentDateTime is required")
    @Future(message = "newAppointmentDateTime must be in the future")
    private LocalDateTime newAppointmentDateTime;
}
