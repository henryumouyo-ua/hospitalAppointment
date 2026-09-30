package com.hospital.appointment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Column(nullable = false)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9()\\-\\s]{7,20}$", message = "Phone number format is invalid")
    @Column(nullable = false)
    private String phoneNumber;

    // Soft-delete flag. "Deleting" a patient sets this to false rather than
    // removing the row, so existing appointment history is never lost.
    @Column(nullable = false)
    private boolean active = true;

    // cascade limited to PERSIST/MERGE only: deleting a Patient entity in
    // code must no longer be able to cascade-delete their appointment
    // history. Deletion is handled at the service layer via the active flag.
    @OneToMany(mappedBy = "patient", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<Appointment> appointments = new ArrayList<>();
}
