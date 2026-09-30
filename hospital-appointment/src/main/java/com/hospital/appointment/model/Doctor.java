package com.hospital.appointment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Column(nullable = false)
    private String fullName;

    @NotBlank(message = "Specialization is required")
    @Column(nullable = false)
    private String specialization;

    // Soft-delete flag. "Deleting" a doctor sets this to false rather than
    // removing the row, so existing appointment history is never lost.
    @Column(nullable = false)
    private boolean active = true;

    // cascade limited to PERSIST/MERGE only: deleting a Doctor entity in
    // code must no longer be able to cascade-delete their appointment
    // history. Deletion is handled at the service layer via the active flag.
    @OneToMany(mappedBy = "doctor", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<Appointment> appointments = new ArrayList<>();
}
